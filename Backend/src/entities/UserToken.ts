import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    OneToMany,
    Relation,
    ManyToMany,
    JoinTable,
    OneToOne,
    JoinColumn,
} from "typeorm";
import { Workout } from "./Workout";
import { CustomExerciseRef } from "./CustomExerciseRef";
import { Template } from "./Template";
import { generateRandomToken } from "../utils/password";
import { User } from "./User";

@Entity()
export class UserToken {
    @PrimaryGeneratedColumn()
    id: number;

    @OneToOne(() => User, (user) => user.token)
    @JoinColumn()
    user: Relation<User>;

    @Column({ default: Date.now })
    createdAt: number; // we want this to expire every 30 * 86400

    @Column()
    token: string;
}
