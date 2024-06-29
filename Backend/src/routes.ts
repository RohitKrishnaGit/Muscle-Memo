import { UserController } from "./controllers/UserController";
import { ExerciseRefController } from "./controllers/ExerciseRefController";
import { CustomExerciseRefController } from "./controllers/CustomExerciseRefController";
import { WorkoutController } from "./controllers/WorkoutController";
import { ExerciseController } from "./controllers/ExerciseController";
import { TemplateController } from "./controllers/TemplateController";
import { authenticateWithToken } from "./middleware/auth";

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
        method: "get",
        route: "/users",
        controller: UserController,
        middleware: [],
        action: "all",
    },
    {
        method: "post",
        route: "/users",
        controller: UserController,
        middleware: [],
        action: "save",
    },
    {
        method: "get",
        route: "/users/:id",
        controller: UserController,
        middleware: [],
        action: "one",
    },
    {
        method: "delete",
        route: "/users/:id",
        controller: UserController,
        middleware: [],
        action: "remove",
    },

    /* exerciseRef routes */
    {
        method: "get",
        route: "/exerciseRefs/:id",
        controller: ExerciseRefController,
        middleware: [],
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
    //     action: "save",
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
        middleware: [],
        action: "one",
    },
    {
        method: "get",
        route: "/customExerciseRefs/:userId",
        controller: CustomExerciseRefController,
        middleware: [],
        action: "all",
    },
    {
        method: "post",
        route: "/customExerciseRefs",
        controller: CustomExerciseRefController,
        middleware: [],
        action: "save",
    },
    {
        method: "delete",
        route: "/customExerciseRefs/:userId/:id",
        controller: CustomExerciseRefController,
        middleware: [],
        action: "remove",
    },

    /* Workout routes */
    /* Shouldn't need to work with ALL workouts, just those per user */
    {
        method: "get",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [],
        action: "one",
    },
    {
        method: "get",
        route: "/workouts/:userId",
        controller: WorkoutController,
        middleware: [],
        action: "all",
    },
    {
        method: "post",
        route: "/workouts",
        controller: WorkoutController,
        middleware: [],
        action: "save",
    },
    {
        method: "delete",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        middleware: [],
        action: "remove",
    },

    /* Template routes */
    /* Shouldn't need to work with ALL templates, just those per user */
    {
        method: "get",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        middleware: [],
        action: "one",
    },
    {
        method: "get",
        route: "/templates/:userId",
        controller: TemplateController,
        middleware: [],
        action: "all",
    },
    {
        method: "post",
        route: "/templates",
        controller: TemplateController,
        middleware: [],
        action: "save",
    },
    {
        method: "delete",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        middleware: [],
        action: "remove",
    },

    /* Exercise routes */
    /* Shouldn't need to work with ALL exercises, just those per workout */
    {
        method: "get",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [],
        action: "one",
    },
    {
        method: "get",
        route: "/exercises/:workoutId",
        controller: ExerciseController,
        middleware: [],
        action: "all",
    },
    {
        method: "post",
        route: "/exercises",
        controller: ExerciseController,
        middleware: [],
        action: "save",
    },
    {
        method: "delete",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        middleware: [],
        action: "remove",
    },
];
