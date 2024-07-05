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
export class Template {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @ManyToOne(() => User, (user) => user.templates)
    user: User;

    @OneToMany(() => Exercise, (exercise) => exercise.template)
    exercises: Relation<Exercise[]>;
}
