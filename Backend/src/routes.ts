import { UserController } from "./controller/UserController";
import { ExerciseRefController } from "./controller/ExerciseRefController";
import { CustomExerciseRefController } from "./controller/CustomExerciseRefController";
import { WorkoutController } from "./controller/WorkoutController";
import { ExerciseController } from "./controller/ExerciseController";
import { TemplateController } from "./controller/TemplateController";

export const Routes = [

    /* User routes */
    {
        method: "get",
        route: "/users/login",
        controller: UserController,
        action: "login",
    },
    {
        method: "get",
        route: "/users/:id",
        controller: UserController,
        action: "one",
    },
    {
        method: "get",
        route: "/users",
        controller: UserController,
        action: "all",
    },
    {
        method: "post",
        route: "/users",
        controller: UserController,
        action: "save",
    },
    {
        method: "delete",
        route: "/users/:id",
        controller: UserController,
        action: "remove",
    },
    

    /* exerciseRef routes */
    {
        method: "get",
        route: "/exerciseRefs/:id",
        controller: ExerciseRefController,
        action: "one",
    },
    {
        method: "get",
        route: "/exerciseRefs",
        controller: ExerciseRefController,
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
        action: "one",
    },
    {
        method: "get",
        route: "/customExerciseRefs/:userId",
        controller: CustomExerciseRefController,
        action: "all",
    },
    {
        method: "post",
        route: "/customExerciseRefs",
        controller: CustomExerciseRefController,
        action: "save",
    },
    {
        method: "delete",
        route: "/customExerciseRefs/:userId/:id",
        controller: CustomExerciseRefController,
        action: "remove",
    },

    /* Workout routes */
    /* Shouldn't need to work with ALL workouts, just those per user */
    {
        method: "get",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        action: "one",
    },
    {
        method: "get",
        route: "/workouts/:userId",
        controller: WorkoutController,
        action: "all",
    },
    {
        method: "post",
        route: "/workouts",
        controller: WorkoutController,
        action: "save",
    },
    {
        method: "delete",
        route: "/workouts/:userId/:id",
        controller: WorkoutController,
        action: "remove",
    },

    /* Template routes */
    /* Shouldn't need to work with ALL templates, just those per user */
    {
        method: "get",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        action: "one",
    },
    {
        method: "get",
        route: "/templates/:userId",
        controller: TemplateController,
        action: "all",
    },
    {
        method: "post",
        route: "/templates",
        controller: TemplateController,
        action: "save",
    },
    {
        method: "delete",
        route: "/templates/:userId/:id",
        controller: TemplateController,
        action: "remove",
    },


    /* Exercise routes */
    /* Shouldn't need to work with ALL exercises, just those per workout */
    {
        method: "get",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        action: "one",
    },
    {
        method: "get",
        route: "/exercises/:workoutId",
        controller: ExerciseController,
        action: "all",
    },
    {
        method: "post",
        route: "/exercises",
        controller: ExerciseController,
        action: "save",
    },
    {
        method: "delete",
        route: "/exercises/:workoutId/:id",
        controller: ExerciseController,
        action: "remove",
    },
];
