import { NextFunction, Request, Response } from "express";
import { totp } from "otplib";
import { AppDataSource } from "../data-source";
import { AllowedStatistics } from "../entities/AllowedStatistics";
import { User } from "../entities/User";
import { UserPrs } from "../entities/UserPrs";
import { UserToken } from "../entities/UserToken";
import { sendEmail } from "../services/EmailService";
import { passwordResetHtml, reportHtml } from "../utils/emailPresets";
import { generatePasswordHash, validatePassword } from "../utils/password";
import { failure, success } from "../utils/responseTypes";
import { generateTokens } from "../utils/token";

export class UserController {
    private userRepository = AppDataSource.getRepository(User);
    private userTokenRepository = AppDataSource.getRepository(UserToken);

    async all(request: Request, response: Response, next: NextFunction) {
        return success(this.userRepository.find());
    }

    async oneHelper(userId: string) {
        const id = parseInt(userId);

        return this.userRepository.findOneBy({
            id,
        });
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const id = request.params.id;

        const user = await this.oneHelper(id);

        if (!user) {
            return failure("unregistered user");
        }
        return success(user);
    }

    async getFriends(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user.friends);
    }

    async getIncomingFriendRequests(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                incomingFriendRequests: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user.incomingFriendRequests);
    }

    async getOutgoingFriendRequests(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                outgoingFriendRequests: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user.outgoingFriendRequests);
    }

    async sendFriendRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);
        const { friendId } = request.body;

        console.log({ friendId });

        console.log(request.body);

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                outgoingFriendRequests: true,
            },
        });

        console.log({ user });

        const friend = await this.userRepository.findOneBy({
            id: friendId,
        });

        console.log({ friend });

        if (!user) {
            return failure("unregistered user");
        }

        if (!friend) {
            return failure("target user does not exist");
        }

        if (friend.id === user.id) {
            return failure("cannot send friend request to self");
        }

        if (user.outgoingFriendRequests.includes(friend)) {
            return failure("friend request already sent to this user");
        }

        user.outgoingFriendRequests = [
            ...(user.outgoingFriendRequests ?? []),
            friend,
        ];

        return success(this.userRepository.save(user));
    }

    async acceptFriendRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                friends: true,
            },
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                outgoingFriendRequests: true,
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        if (!friend) {
            return failure("target user does not exist");
        }

        const len = friend.outgoingFriendRequests.length;
        friend.outgoingFriendRequests = friend.outgoingFriendRequests?.filter(
            (friendReq) => {
                return friendReq.id !== user.id;
            }
        );

        if (friend.outgoingFriendRequests.length === len) {
            return failure("this friend request does not exist");
        }

        user.friends = [...(user.friends ?? []), friend];
        friend.friends = [...(friend.friends ?? []), user];

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend request accept successs");
    }

    async rejectFriendRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                friends: true,
            },
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                outgoingFriendRequests: true,
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        if (!friend) {
            return failure("target user does not exist");
        }

        const len = friend.outgoingFriendRequests.length;
        friend.outgoingFriendRequests = friend.outgoingFriendRequests?.filter(
            (friendReq) => {
                return friendReq.id !== user.id;
            }
        );

        if (friend.outgoingFriendRequests.length === len) {
            return failure("this friend request does not exist");
        }

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend request accept successs");
    }

    async removeFriend(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                friends: true,
            },
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        if (!friend) {
            return failure("target user does not exist");
        }

        const len1 = user.friends.length;
        user.friends = user.friends?.filter((userFriend) => {
            return userFriend.id !== friendId;
        });

        const len2 = friend.friends.length;
        friend.friends = friend.friends?.filter((friendFriend) => {
            return friendFriend.id !== userId;
        });

        if (user.friends.length === len1 || friend.friends.length === len2) {
            return failure("this friend does not exist");
        }

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend removed");
    }

    async login(request: Request, response: Response, next: NextFunction) {
        const { email, password } = request.body;

        console.log("jdiowajdiowa");
        console.log({ email, password });

        const user = await this.userRepository.findOne({
            where: {
                email,
            },
            select: ["id", "password", "role"],
        });

        console.log({ user });

        if (!user) {
            return failure("Invalid email or password", 401);
        }

        const verifiedPassword = await validatePassword(
            password,
            user.password
        );

        console.log(verifiedPassword);

        if (!verifiedPassword) {
            return failure("Invalid email or password", 401);
        }

        return success(generateTokens(user));
    }

    async logout(request: Request, response: Response, next: NextFunction) {
        let { refreshToken, firebaseToken } = request.body;
        const id = parseInt(request.params.userId);
        const userToken = await this.userTokenRepository.findOneBy({
            token: refreshToken,
        });
        if (!userToken) {
            return failure("Refresh token is invalid");
        }
        await this.userTokenRepository.remove(userToken);

        const userToUpdate = await this.userRepository.findOneBy({
            id,
        });

        if (!userToUpdate) return failure("Update failed");

        let listOfTokens = JSON.parse(userToUpdate.firebaseTokens);

        listOfTokens = JSON.stringify(
            listOfTokens.filter((token: string) => token != firebaseToken)
        );

        await this.userRepository.save({
            ...userToUpdate,
            firebaseTokens: listOfTokens,
        });
        return success("Successfully logged out");
    }

    async logoutAll(request: Request, response: Response, next: NextFunction) {
        const userTokens = await this.userTokenRepository.findBy({
            user: { id: request.user?.id },
        });
        await this.userTokenRepository.remove(userTokens);
        return success("Successfully logged out");
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const {
            username,
            email,
            password,
            gender,
            experience,
            profilePicture,
        } = request.body;

        const userExists = !!(await this.userRepository.findOneBy({ email }));

        if (userExists) {
            return failure("User with given email already exist");
        }

        const user = Object.assign(new User(), {
            username,
            email,
            password: await generatePasswordHash(password),
            gender,
            experience,
            userPrs: new UserPrs(),
            allowedStatistics: new AllowedStatistics(),
            profilePicture,
        });

        await this.userRepository.save(user);

        return success("User created");
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        let userToRemove = await this.userRepository.findOneBy({
            id,
        });

        if (!userToRemove) {
            return failure("this user not exist");
        }

        await this.userRepository.remove(userToRemove);

        return success("user has been removed");
    }

    async update(request: Request, response: Response, next: NextFunction) {
        const { username, gender, experience, profilePicture } = request.body;
        const id = parseInt(request.params.userId);
        let userToUpdate = await this.userRepository.findOneBy({
            id,
        });
        if (!userToUpdate) return failure("Update failed");
        return success(
            this.userRepository.save({
                ...userToUpdate,
                username,
                gender,
                experience,
                profilePicture,
            })
        );
    }

    async updateFirebaseToken(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        let { firebaseTokens } = request.body;
        console.log(firebaseTokens);
        const id = parseInt(request.params.userId);
        let userToUpdate = await this.userRepository.findOneBy({
            id,
        });
        if (userToUpdate) {
            let listOfTokens = JSON.parse(userToUpdate.firebaseTokens);
            if (listOfTokens) {
                if (!listOfTokens.includes(firebaseTokens)) {
                    listOfTokens.push(firebaseTokens);
                }
            }
            firebaseTokens = JSON.stringify(listOfTokens);
            console.log(listOfTokens);
        }
        if (!userToUpdate) return failure("Update failed");
        return success(
            this.userRepository.save({
                ...userToUpdate,
                firebaseTokens,
            })
        );
    }

    async findEmail(request: Request, response: Response, next: NextFunction) {
        const email = request.params.email;
        let users = await this.userRepository.find({
            where: {
                email: email,
            },
        });
        if (users.length != 0) {
            return success(true);
        }
        return success(false);
    }

    async reportUser(request: Request, response: Response, next: NextFunction) {
        const { reportedUserId, reason } = request.body;
        const reporterId = request.user?.id;

        if (!reporterId) return failure("Access Token Error");

        if (reportedUserId == reporterId) {
            return failure("Cannot report yourself")
        }

        const reporter = await this.userRepository.findOneBy({
            id: reporterId,
        });
        const reportee = await this.userRepository.findOneBy({
            id: reportedUserId,
        });

        if (!reporter) return failure("Reporting user does not exist");
        if (!reportee) return failure("Reported user does not exist");

        await sendEmail(
            "musclememo.help@gmail.com",
            "User Reported",
            reportHtml(
                `${reporter.username} (${reporter.email})`,
                `${reportee.username} (${reportee.email})`,
                reason
            )
        );

        return success("User Reported");
    }

    async findUsername(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const username = request.params.username;
        let users = await this.userRepository.find({
            where: {
                username: username,
            },
        });
        if (users.length != 0) {
            return success(users[0].id);
        }
        return failure("User not found");
    }

    async requestPasswordReset(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const { email } = request.body;

        const user = await this.userRepository.findOneBy({ email });
        if (!user) return failure("Email not found");

        const code = totp.generate(`${user.id}-${user.email}`);

        await this.userRepository.save({ ...user, passwordResetToken: code });

        sendEmail(email, "Password Reset", passwordResetHtml(code));

        return success("Code sent");
    }
    async confirmPasswordReset(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const code = request.params.code;

        const user = await this.userRepository.findOneBy({
            passwordResetToken: code,
        });
        if (!user) return failure("User not found");

        const isValid = totp.verify({
            token: code,
            secret: `${user.id}-${user.email}`,
        });

        if (!isValid) return failure("Code is invalid");

        return success("Code is valid");
    }
    async resetPassword(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const code = request.params.code;
        const { password } = request.body;

        const user = await this.userRepository.findOneBy({
            passwordResetToken: code,
        });
        if (!user) return failure("User not found");

        await this.userRepository.save({
            ...user,
            password: await generatePasswordHash(password),
            passwordResetToken: null,
        });

        return success("Password Updated");
    }
}
