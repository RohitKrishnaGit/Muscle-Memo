import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    OneToMany,
    Relation,
    ManyToMany,
    JoinTable,
    OneToOne,
} from "typeorm";
import { Workout } from "./Workout";
import { CustomExerciseRef } from "./CustomExerciseRef";
import { Template } from "./Template";
import { generateRandomToken } from "../utils/password";
import { UserToken } from "./UserToken";

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
    friends?: Relation<User[]>;

    @OneToMany(() => Workout, (workout) => workout.user)
    workouts?: Relation<Workout[]>;

    @OneToMany(() => Template, (template) => template.user)
    templates?: Relation<Template[]>;

    @OneToMany(() => CustomExerciseRef, (customExercise) => customExercise.user)
    customExercises?: Relation<CustomExerciseRef[]>;

    @Column({ default: Date.now })
    createdAt: number;

    @Column({ default: Date.now })
    lastLoggedIn: number;

    @OneToOne(() => UserToken, (userToken) => userToken.user, {
        nullable: true,
    })
    token?: Relation<UserToken>;
}
