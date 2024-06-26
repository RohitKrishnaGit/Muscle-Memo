import {
    Column,
    Entity,
    ManyToOne,
    OneToMany,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { User } from "./User";
import { Exercise } from "./Exercise";

@Entity()
export class Template {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @ManyToOne(() => User, (user) => user.workouts)
    user: User;

    @OneToMany(() => Exercise, (exercise) => exercise.workout)
    exercises: Relation<Exercise[]>;
}
