import { AllowedStatisticsController } from "./controllers/AllowedStatsisticsController";
import { CombinedExerciseRefController } from "./controllers/CombinedExerciseRefController";
import { CustomExerciseRefController } from "./controllers/CustomExerciseRefController";
import { ExerciseController } from "./controllers/ExerciseController";
import { ExerciseRefController } from "./controllers/ExerciseRefController";
import { NotificationController } from "./controllers/NotificationController";
import { PublicWorkoutController } from "./controllers/PublicWorkoutController";
import { TemplateController } from "./controllers/TemplateController";
import { TokenController } from "./controllers/TokenController";
import { UserController } from "./controllers/UserController";
import { UserPrsController } from "./controllers/UserPRsController";
import { WorkoutController } from "./controllers/WorkoutController";
import {
    applyUser,
    authenticateWithToken,
    convertMe,
    isAdmin,
    sameUser,
} from "./middleware/auth";
import { enforce, or } from "./middleware/enforce";
import { validateSchema } from "./middleware/validation";
import { allCombinedExerciseRefSchema } from "./schemas/combinedExerciseRefSchema";
import {
    allCustomExerciseRefSchema,
    createCustomExerciseRefSchema,
    oneCustomExerciseRefSchema,
    removeCustomExerciseRefSchema,
    updateCustomExerciseRefSchema,
} from "./schemas/customExerciseSchema";
import {
    allExerciseRefSchema,
    oneExerciseRefSchema,
} from "./schemas/exerciseRefSchema";
import {
    allExerciseSchema,
    createExerciseSchema,
    oneExerciseSchema,
    removeExerciseSchema,
} from "./schemas/exerciseSchema";
import {
    getAllPrSchema,
    getPrSchema,
    getPrVisibilitySchema,
    leaderboardFriendsSchema,
    leaderboardSchema,
    postPrSchema,
    postPrVisibilitySchema,
} from "./schemas/leaderboardPRSchema";
import {
    allPublicWorkoutSchema,
    createPublicWorkoutSchema,
    filterPublicWorkoutSchema,
    onePublicWorkoutSchema,
    removePublicWorkoutSchema,
} from "./schemas/publicWorkoutSchema";
import {
    allTemplateSchema,
    createTemplateSchema,
    oneTemplateSchema,
    removeTemplateSchema,
} from "./schemas/templateSchema";
import { newTokenSchema } from "./schemas/tokenSchema";
import {
    acceptFriendReqSchema,
    allUserSchema,
    confirmPasswordResetSchema,
    createUserSchema,
    getFriendsSchema,
    incomingFriendReqSchema,
    loginUserSchema,
    logoutAllUserSchema,
    logoutSchema,
    oneUserSchema,
    outgoingFriendReqSchema,
    rejectFriendReqSchema,
    removeFriendSchema,
    removeUserSchema,
    reportUserSchema,
    requestPasswordResetSchema,
    resetPasswordSchema,
    sendFriendReqSchema,
    updateUserFirebaseTokenSchema,
    updateUserSchema,
} from "./schemas/userSchema";
import {
    allWorkoutSchema,
    createWorkoutSchema,
    oneWorkoutSchema,
    removeWorkoutSchema,
} from "./schemas/workoutSchema";

import { ChatController } from "./controllers/ChatController";
import { allChatsSchema, removeChatsSchema } from "./schemas/chatSchema";
import { sendNotificationSchema } from "./schemas/notificationSchema";

export const Routes = [
    /* User routes */
    {
        method: "post",
        route: "/users/login",
        controller: UserController,
        middleware: [validateSchema(loginUserSchema)],
        action: "login",
    },
    {
        method: "post",
        route: "/users/logout/:userId",
        controller: UserController,
        middleware: [
            validateSchema(logoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "logout",
    },
    {
        method: "delete",
        route: "/users/logoutAll",
        controller: UserController,
        middleware: [
            validateSchema(logoutAllUserSchema),
            authenticateWithToken,
        ],
        action: "logoutAll",
    },
    {
        method: "post",
        route: "/users/register",
        controller: UserController,
        middleware: [validateSchema(createUserSchema)],
        action: "create",
    },
    {
        method: "get",
        route: "/users/:userId/friends",
        controller: UserController,
        middleware: [
            validateSchema(getFriendsSchema),
            authenticateWithToken,
            ...applyUser(["params", "userId"], convertMe),
        ],
        action: "getFriends",
    },
    {
        method: "get",
        route: "/users/:userId/incomingFriendRequests",
        controller: UserController,
        middleware: [
            validateSchema(incomingFriendReqSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "getIncomingFriendRequests",
    },
    {
        method: "get",
        route: "/users/:userId/outgoingFriendRequests",
        controller: UserController,
        middleware: [
            validateSchema(outgoingFriendReqSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "getOutgoingFriendRequests",
    },
    {
        method: "post",
        route: "/users/:userId/sendFriendRequest",
        controller: UserController,
        middleware: [
            validateSchema(sendFriendReqSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "sendFriendRequest",
    },
    {
        method: "post",
        route: "/users/:userId/acceptFriendRequest",
        controller: UserController,
        middleware: [
            validateSchema(acceptFriendReqSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "acceptFriendRequest",
    },
    {
        method: "post",
        route: "/users/:userId/rejectFriendRequest",
        controller: UserController,
        middleware: [
            validateSchema(rejectFriendReqSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "rejectFriendRequest",
    },
    {
        method: "post",
        route: "/users/:userId/removeFriend",
        controller: UserController,
        middleware: [
            validateSchema(removeFriendSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "removeFriend",
    },
    {
        method: "get",
        route: "/users/:id",
        controller: UserController,
        middleware: [
            validateSchema(oneUserSchema),
            authenticateWithToken,
            ...applyUser(["params", "id"], convertMe),
        ],
        action: "one",
    },
    {
        method: "delete",
        route: "/users/:id",
        controller: UserController,
        middleware: [
            validateSchema(removeUserSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "id"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "remove",
    },
    {
        method: "get",
        route: "/users",
        controller: UserController,
        middleware: [validateSchema(allUserSchema), authenticateWithToken],
        action: "all",
    },
    {
        method: "put",
        route: "/users/update/:userId",
        controller: UserController,
        middleware: [
            validateSchema(updateUserSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "update",
    },

    {
        method: "get",
        route: "/users/email/:email",
        controller: UserController,
        middleware: [validateSchema(allUserSchema)],
        action: "findEmail",
    },

    {
        method: "put",
        route: "/users/update/firebase/:userId",
        controller: UserController,
        middleware: [
            validateSchema(updateUserFirebaseTokenSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "updateFirebaseToken",
    },

    {
        method: "post",
        route: "/users/report",
        controller: UserController,
        middleware: [validateSchema(reportUserSchema), authenticateWithToken],
        action: "reportUser",
    },

    {
        method: "post",
        route: "/users/reset-password/request",
        controller: UserController,
        middleware: [validateSchema(requestPasswordResetSchema)],
        action: "requestPasswordReset",
    },

    {
        method: "post",
        route: "/users/reset-password/confirm/:code",
        controller: UserController,
        middleware: [validateSchema(confirmPasswordResetSchema)],
        action: "confirmPasswordReset",
    },

    {
        method: "post",
        route: "/users/reset-password/:code",
        controller: UserController,
        middleware: [validateSchema(resetPasswordSchema)],
        action: "resetPassword",
    },

    /* exerciseRef routes */
    {
        method: "get",
        route: "/exerciseRefs/:id",
        controller: ExerciseRefController,
        middleware: [
            validateSchema(oneExerciseRefSchema),
            authenticateWithToken,
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/exerciseRefs",
        controller: ExerciseRefController,
        middleware: [
            validateSchema(allExerciseRefSchema),
            authenticateWithToken,
        ],
        action: "all",
    },
    {
        method: "get",
        route: "/customExerciseRefs/:userId/:id",
        controller: CustomExerciseRefController,
        middleware: [
            validateSchema(oneCustomExerciseRefSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/customExerciseRefs/:userId",
        controller: CustomExerciseRefController,
        middleware: [
            validateSchema(allCustomExerciseRefSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "all",
    },
    {
        method: "post",
        route: "/customExerciseRefs/:userId",
        controller: CustomExerciseRefController,
        middleware: [
            validateSchema(createCustomExerciseRefSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "create",
    },
    {
        method: "put",
        route: "/customExerciseRefs/:userId/:id",
        controller: CustomExerciseRefController,
        middleware: [
            validateSchema(updateCustomExerciseRefSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "update",
    },
    {
        method: "delete",
        route: "/customExerciseRefs/:userId/:id",
        controller: CustomExerciseRefController,
        middleware: [
            validateSchema(removeCustomExerciseRefSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "remove",
    },

    {
        method: "get",
        route: "/combinedExerciseRefs/:userId",
        controller: CombinedExerciseRefController,
        middleware: [
            validateSchema(allCombinedExerciseRefSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "all",
    },

    /* Workout routes */
    /* Shouldn't need to work with ALL workouts, just those per user */
    {
        method: "get",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [
            validateSchema(oneWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/workouts/:userId",
        controller: WorkoutController,
        middleware: [
            validateSchema(allWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "all",
    },
    {
        method: "post",
        route: "/workouts",
        controller: WorkoutController,
        middleware: [
            validateSchema(createWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["body", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [
            validateSchema(removeWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "remove",
    },

    /* Template routes */
    /* Shouldn't need to work with ALL templates, just those per user */
    {
        method: "get",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        middleware: [
            validateSchema(oneTemplateSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/templates/:userId",
        controller: TemplateController,
        middleware: [
            validateSchema(allTemplateSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "all",
    },
    {
        method: "post",
        route: "/templates",
        controller: TemplateController,
        middleware: [
            validateSchema(createTemplateSchema),
            authenticateWithToken,
            ...applyUser(
                ["body", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        middleware: [
            validateSchema(removeTemplateSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "remove",
    },

    /* Exercise routes */
    /* Shouldn't need to work with ALL exercises, just those per workout */
    {
        method: "get",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [validateSchema(oneExerciseSchema), authenticateWithToken],
        action: "one",
    },
    {
        method: "get",
        route: "/exercises/:workoutId",
        controller: ExerciseController,
        middleware: [validateSchema(allExerciseSchema), authenticateWithToken],
        action: "all",
    },
    {
        method: "post",
        route: "/exercises",
        controller: ExerciseController,
        middleware: [
            validateSchema(createExerciseSchema),
            authenticateWithToken,
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [
            validateSchema(removeExerciseSchema),
            authenticateWithToken,
        ],
        action: "remove",
    },

    /* Token routes */
    {
        method: "post",
        route: "/token",
        controller: TokenController,
        middleware: [validateSchema(newTokenSchema)],
        action: "newToken",
    },

    /* Pr & leaderboard routes */
    {
        method: "get",
        route: "/Pr/:userId/:exerciseRefId",
        controller: UserPrsController,
        middleware: [
            validateSchema(getPrSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "getUserPr",
    },
    {
        method: "post",
        route: "/Pr/:userId/:exerciseRefId",
        controller: UserPrsController,
        middleware: [
            validateSchema(postPrSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "updateUserPr",
    },
    {
        method: "get",
        route: "/leaderboard/:exerciseRefId/:count",
        controller: UserPrsController,
        middleware: [validateSchema(leaderboardSchema)],
        action: "getTopN",
    },
    {
        method: "get",
        route: "/leaderboard/:userId/:exerciseRefId/:count",
        controller: UserPrsController,
        middleware: [validateSchema(leaderboardFriendsSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            )
        ],
        action: "getTopNFriends",
    },
    {
        method: "get",
        route: "/Pr/visibility/:userId/:exerciseRefId",
        controller: AllowedStatisticsController,
        middleware: [
            validateSchema(getPrVisibilitySchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "getAllowedStatistics",
    },
    {
        method: "post",
        route: "/Pr/visibility/:userId/:exerciseRefId",
        controller: AllowedStatisticsController,
        middleware: [
            validateSchema(postPrVisibilitySchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "updateAllowedStatistics",
    },
    {
        method: "get",
        route: "/Pr/:userId/",
        controller: UserPrsController,
        middleware: [
            validateSchema(getAllPrSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "getAllUserPrs",
    },

    /* Notification Controller */
    {
        method: "post",
        route: "/notification",
        controller: NotificationController,
        middleware: [
            validateSchema(sendNotificationSchema),
            authenticateWithToken,
        ],
        action: "notification",
    },

    /* Public workouts routes */
    {
        method: "get",
        route: "/publicWorkouts/filter",
        controller: PublicWorkoutController,
        middleware: [
            validateSchema(filterPublicWorkoutSchema),
            authenticateWithToken,
        ],
        action: "filter",
    },
    {
        method: "get",
        route: "/publicWorkouts/:userId/:id",
        controller: PublicWorkoutController,
        middleware: [
            validateSchema(onePublicWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "one",
    },
    {
        method: "post",
        route: "/publicWorkouts/:userId",
        controller: PublicWorkoutController,
        middleware: [
            validateSchema(createPublicWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/publicWorkouts/:userId/:id",
        controller: PublicWorkoutController,
        middleware: [
            validateSchema(removePublicWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "remove",
    },
    {
        method: "get",
        route: "/publicWorkouts/:userId",
        controller: PublicWorkoutController,
        middleware: [
            validateSchema(allPublicWorkoutSchema),
            authenticateWithToken,
            ...applyUser(
                ["params", "userId"],
                convertMe,
                enforce(or(sameUser, isAdmin))
            ),
        ],
        action: "all",
    },

    /* Chat routes */
    {
        method: "get",
        route: "/chat/:roomId",
        controller: ChatController,
        middleware: [validateSchema(allChatsSchema), authenticateWithToken],
        action: "all",
    },
    {
        method: "delete",
        route: "/chat/:roomId",
        controller: ChatController,
        middleware: [validateSchema(removeChatsSchema), authenticateWithToken],
        action: "remove",
    },
];
