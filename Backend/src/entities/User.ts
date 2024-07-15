import {
    Column,
    Entity,
    JoinTable,
    ManyToMany,
    OneToMany,
    OneToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { CustomExerciseRef } from "./CustomExerciseRef";
import { Template } from "./Template";
import { UserToken } from "./UserToken";
import { Workout } from "./Workout";

export enum Role {
    ADMIN,
    USER,
}

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

    @Column({ select: false })
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

    @OneToOne(() => UserToken, (userToken) => userToken.user, {
        nullable: true,
    })
    token?: Relation<UserToken>;

    @Column({ select: false, default: Role.USER })
    role: Role;
}
