import {
    Column,
    Entity,
    ManyToOne,
    OneToMany,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { Exercise } from "./Exercise";
import { User } from "./User";

@Entity()
export class Workout {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @Column()
    isFinished: number;

    @ManyToOne(() => User, (user) => user.workouts)
    user: User;

    @OneToMany(() => Exercise, (exercise) => exercise.workout)
    exercises: Relation<Exercise[]>;
}
