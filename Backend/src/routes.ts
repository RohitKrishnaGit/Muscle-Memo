import { CustomExerciseRefController } from "./controllers/CustomExerciseRefController";
import { ExerciseController } from "./controllers/ExerciseController";
import { ExerciseRefController } from "./controllers/ExerciseRefController";
import { TemplateController } from "./controllers/TemplateController";
import { TokenController } from "./controllers/TokenController";
import { UserController } from "./controllers/UserController";
import { WorkoutController } from "./controllers/WorkoutController";
import {
    authenticateWithToken,
    validateUserBody,
    validateUserParam,
} from "./middleware/auth";
import { validateSchema } from "./middleware/validation";
import {
    allCustomExerciseRefSchema,
    createCustomExerciseRefSchema,
    oneCustomExerciseRefSchema,
    removeCustomExerciseRefSchema,
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
    logoutUserSchema,
    oneUserSchema,
    removeUserSchema,
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
        route: "/users/logout",
        controller: UserController,
        middleware: [validateSchema(logoutUserSchema)],
        action: "logout",
    },
    {
        method: "get",
        route: "/users",
        controller: UserController,
        middleware: [authenticateWithToken, validateSchema(allUserSchema)],
        action: "all",
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
        route: "/users/:id",
        controller: UserController,
        middleware: [authenticateWithToken, validateSchema(oneUserSchema)],
        action: "one",
    },
    {
        method: "delete",
        route: "/users/:id",
        controller: UserController,
        middleware: [
            authenticateWithToken,
            validateUserParam("id"),
            validateSchema(removeUserSchema),
        ],
        action: "remove",
    },

    /* exerciseRef routes */
    {
        method: "get",
        route: "/exerciseRefs/:id",
        controller: ExerciseRefController,
        middleware: [
            authenticateWithToken,
            validateSchema(oneExerciseRefSchema),
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/exerciseRefs",
        controller: ExerciseRefController,
        middleware: [
            authenticateWithToken,
            validateSchema(allExerciseRefSchema),
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
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(oneCustomExerciseRefSchema),
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/customExerciseRefs/:userId",
        controller: CustomExerciseRefController,
        middleware: [
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(allCustomExerciseRefSchema),
        ],
        action: "all",
    },
    {
        method: "post",
        route: "/customExerciseRefs",
        controller: CustomExerciseRefController,
        middleware: [
            authenticateWithToken,
            validateUserBody("userId"),
            validateSchema(createCustomExerciseRefSchema),
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/customExerciseRefs/:userId/:id",
        controller: CustomExerciseRefController,
        middleware: [
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(removeCustomExerciseRefSchema),
        ],
        action: "remove",
    },

    /* Workout routes */
    /* Shouldn't need to work with ALL workouts, just those per user */
    {
        method: "get",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(oneWorkoutSchema),
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/workouts/:userId",
        controller: WorkoutController,
        middleware: [
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(allWorkoutSchema),
        ],
        action: "all",
    },
    {
        method: "post",
        route: "/workouts",
        controller: WorkoutController,
        middleware: [
            authenticateWithToken,
            validateUserBody("userId"),
            validateSchema(createWorkoutSchema),
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(removeWorkoutSchema),
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
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(oneTemplateSchema),
        ],
        action: "one",
    },
    {
        method: "get",
        route: "/templates/:userId",
        controller: TemplateController,
        middleware: [
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(allTemplateSchema),
        ],
        action: "all",
    },
    {
        method: "post",
        route: "/templates",
        controller: TemplateController,
        middleware: [
            authenticateWithToken,
            validateUserBody("userId"),
            validateSchema(createTemplateSchema),
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        middleware: [
            authenticateWithToken,
            validateUserParam("userId"),
            validateSchema(removeTemplateSchema),
        ],
        action: "remove",
    },

    /* Exercise routes */
    /* Shouldn't need to work with ALL exercises, just those per workout */
    {
        method: "get",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [authenticateWithToken, validateSchema(oneExerciseSchema)],
        action: "one",
    },
    {
        method: "get",
        route: "/exercises/:workoutId",
        controller: ExerciseController,
        middleware: [authenticateWithToken, validateSchema(allExerciseSchema)],
        action: "all",
    },
    {
        method: "post",
        route: "/exercises",
        controller: ExerciseController,
        middleware: [
            authenticateWithToken,
            validateSchema(createExerciseSchema),
        ],
        action: "create",
    },
    {
        method: "delete",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [
            authenticateWithToken,
            validateSchema(removeExerciseSchema),
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
