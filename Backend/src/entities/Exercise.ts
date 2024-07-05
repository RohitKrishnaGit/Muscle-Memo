import {
    Column,
    Entity,
    ManyToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { CustomExerciseRef } from "./CustomExerciseRef";
import { ExerciseRef } from "./ExerciseRef";
import { Template } from "./Template";
import { Workout } from "./Workout";

@Entity()
export class Exercise {
    @PrimaryGeneratedColumn()
    id: number;

    @ManyToOne(() => Workout, (workout) => workout.exercises)
    workout: Relation<Workout>;

    @ManyToOne(() => Template, (template) => template.exercises)
    template: Relation<Workout>;

    @ManyToOne(() => ExerciseRef, { nullable: true })
    exerciseRef?: Relation<ExerciseRef>;

    @ManyToOne(() => CustomExerciseRef, { nullable: true })
    customExerciseRef?: Relation<CustomExerciseRef>;

    @Column({ nullable: true })
    sets?: number;

    @Column({ nullable: true })
    reps?: number;

    @Column({ nullable: true })
    weight?: number;

    @Column({ nullable: true })
    duration?: number;
}
