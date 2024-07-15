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

export const Routes = [
    /* User routes */
    {
        method: "post",
        route: "/users/login",
        controller: UserController,
        middleware: [],
        action: "login",
    },
    {
        method: "delete",
        route: "/users/logout",
        controller: UserController,
        middleware: [],
        action: "logout",
    },
    {
        method: "post",
        route: "/users/register",
        controller: UserController,
        middleware: [],
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
        middleware: [authenticateWithToken],
        action: "one",
    },
    {
        method: "delete",
        route: "/users/:id",
        controller: UserController,
        middleware: [authenticateWithToken, validateUserParam("id")],
        action: "remove",
    },
    {
        method: "get",
        route: "/users",
        controller: UserController,
        middleware: [authenticateWithToken],
        action: "all",
    },

    /* exerciseRef routes */
    {
        method: "get",
        route: "/exerciseRefs/:id",
        controller: ExerciseRefController,
        middleware: [authenticateWithToken],
        action: "one",
    },
    {
        method: "get",
        route: "/exerciseRefs",
        controller: ExerciseRefController,
        middleware: [authenticateWithToken],
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
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "one",
    },
    {
        method: "get",
        route: "/customExerciseRefs/:userId",
        controller: CustomExerciseRefController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "all",
    },
    {
        method: "post",
        route: "/customExerciseRefs",
        controller: CustomExerciseRefController,
        middleware: [authenticateWithToken, validateUserBody("userId")],
        action: "create",
    },
    {
        method: "delete",
        route: "/customExerciseRefs/:userId/:id",
        controller: CustomExerciseRefController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "remove",
    },

    /* Workout routes */
    /* Shouldn't need to work with ALL workouts, just those per user */
    {
        method: "get",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "one",
    },
    {
        method: "get",
        route: "/workouts/:userId",
        controller: WorkoutController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "all",
    },
    {
        method: "post",
        route: "/workouts",
        controller: WorkoutController,
        middleware: [authenticateWithToken, validateUserBody("userId")],
        action: "create",
    },
    {
        method: "delete",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "remove",
    },

    /* Template routes */
    /* Shouldn't need to work with ALL templates, just those per user */
    {
        method: "get",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "one",
    },
    {
        method: "get",
        route: "/templates/:userId",
        controller: TemplateController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "all",
    },
    {
        method: "post",
        route: "/templates",
        controller: TemplateController,
        middleware: [authenticateWithToken, validateUserBody("userId")],
        action: "create",
    },
    {
        method: "delete",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        middleware: [authenticateWithToken, validateUserParam("userId")],
        action: "remove",
    },

    /* Exercise routes */
    /* Shouldn't need to work with ALL exercises, just those per workout */
    {
        method: "get",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [authenticateWithToken],
        action: "one",
    },
    {
        method: "get",
        route: "/exercises/:workoutId",
        controller: ExerciseController,
        middleware: [authenticateWithToken],
        action: "all",
    },
    {
        method: "post",
        route: "/exercises",
        controller: ExerciseController,
        middleware: [authenticateWithToken],
        action: "create",
    },
    {
        method: "delete",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [authenticateWithToken],
        action: "remove",
    },

    /* Token routes */
    {
        method: "post",
        route: "/token",
        controller: TokenController,
        middleware: [],
        action: "newToken",
    },
];
