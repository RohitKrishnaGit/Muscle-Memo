import { CombinedExerciseRefController } from "./controllers/CombinedExerciseRefController";
import { CustomExerciseRefController } from "./controllers/CustomExerciseRefController";
import { ExerciseController } from "./controllers/ExerciseController";
import { ExerciseRefController } from "./controllers/ExerciseRefController";
import { TemplateController } from "./controllers/TemplateController";
import { TokenController } from "./controllers/TokenController";
import { UserController } from "./controllers/UserController";
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
    allTemplateSchema,
    createTemplateSchema,
    oneTemplateSchema,
    removeTemplateSchema,
} from "./schemas/templateSchema";
import { newTokenSchema } from "./schemas/tokenSchema";
import {
    allUserSchema,
    createUserSchema,
    loginUserSchema,
    logoutAllUserSchema,
    oneUserSchema,
    removeUserSchema,
    updateUserSchema,
} from "./schemas/userSchema";
import {
    allWorkoutSchema,
    createWorkoutSchema,
    oneWorkoutSchema,
    removeWorkoutSchema,
} from "./schemas/workoutSchema";

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
        route: "/users/:id/friends",
        controller: UserController,
        middleware: [authenticateWithToken],
        action: "getFriends",
    },
    {
        method: "get",
        route: "/users/:id/incomingFriendRequests",
        controller: UserController,
        middleware: [authenticateWithToken],
        action: "getIncomingFriendRequests",
    },
    {
        method: "get",
        route: "/users/:id/outgoingFriendRequests",
        controller: UserController,
        middleware: [authenticateWithToken],
        action: "getOutgoingFriendRequests",
    },
    {
        method: "post",
        route: "/users/:id/sendFriendRequest",
        controller: UserController,
        middleware: [authenticateWithToken],
        action: "sendFriendRequest",
    },
    {
        method: "post",
        route: "/users/:id/acceptFriendRequest",
        controller: UserController,
        middleware: [authenticateWithToken],
        action: "acceptFriendRequest",
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
    /* Shouldn't need these, temporarily keeping until cleanup can be confirmed */
    // {
    //     method: "post",
    //     route: "/exerciseRefs",
    //     controller: ExerciseRefController,
    //     action: "create",
    // },
    // {
    //     method: "delete",
    //     route: "/exerciseRefs/:id",
    //     controller: CustomExerciseRefController,
    //     action: "remove",
    // },

    /* customExerciseRef routes */
    /* Shouldn't need to work with ALL customExerciseRefs, just those per user */
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
];
