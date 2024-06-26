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

    @ManyToOne(() => User, (user) => user.templates)
    user: User;

    @OneToMany(() => Exercise, (exercise) => exercise.template)
    exercises: Relation<Exercise[]>;
}
