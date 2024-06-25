import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    OneToMany,
    Relation,
    ManyToMany,
    JoinTable,
} from "typeorm";
import { Workout } from "./Workout";
import { CustomExerciseRef } from "./CustomExerciseRef";

@Entity()
export class User {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    username: string;

    @Column()
    fullName: string;

    @Column()
    email: string;

    @Column()
    password: string;

    @ManyToMany(() => User)
    @JoinTable()
    friends?: User[];

    @OneToMany(() => Workout, (workout) => workout.user)
    workouts?: Relation<Workout[]>;

    @OneToMany(() => CustomExerciseRef, (customExercise) => customExercise.user)
    customExercises?: Relation<CustomExerciseRef[]>;
}
