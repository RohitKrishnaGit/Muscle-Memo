import {
    Column,
    Entity,
    JoinTable,
    ManyToMany,
    OneToMany,
    OneToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { AllowedStatistics } from "./AllowedStatistics";
import { CustomExerciseRef } from "./CustomExerciseRef";
import { Template } from "./Template";
import { UserPrs } from "./UserPrs";
import { UserToken } from "./UserToken";
import { Workout } from "./Workout";

export enum Role {
    ADMIN,
    USER,
}

@Entity()
export class User {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    username: string;

    @Column()
    email: string;

    @Column({ select: false })
    password: string;

    @Column()
    gender: string;

    @Column()
    experience: string;

    @Column({ default: "[]" })
    firebaseTokens: string;

    @Column({ nullable: true })
    profilePicture: string;

    @ManyToMany(() => User, (user) => user.outgoingFriendRequests)
    incomingFriendRequests: Relation<User[]>;

    @ManyToMany(() => User, (user) => user.incomingFriendRequests, {
        cascade: true,
    })
    @JoinTable()
    outgoingFriendRequests: Relation<User[]>;

    @ManyToMany(() => User, {
        cascade: true,
    })
    @JoinTable()
    friends: Relation<User[]>;

    @OneToMany(() => Workout, (workout) => workout.user)
    workouts?: Relation<Workout[]>;

    @OneToMany(() => Template, (template) => template.user)
    templates?: Relation<Template[]>;

    @OneToMany(() => CustomExerciseRef, (customExercise) => customExercise.user)
    customExercises?: Relation<CustomExerciseRef[]>;

    @OneToMany(() => UserToken, (userToken) => userToken.user)
    tokens: Relation<UserToken[]>;

    @OneToOne(() => UserPrs, (UserPrs) => UserPrs.user, {
        cascade: true,
    })
    userPrs: Relation<UserPrs>;

    @OneToOne(
        () => AllowedStatistics,
        (AllowedStatistics) => AllowedStatistics.user,
        {
            cascade: true,
        }
    )
    allowedStatistics: Relation<AllowedStatistics>;

    @Column({ select: false, type: "text", nullable: true })
    passwordResetToken?: string | null;

    @Column({ select: false, default: Role.USER })
    role: Role;
}
