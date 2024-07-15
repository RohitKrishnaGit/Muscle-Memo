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

    @ManyToMany(() => User, (user) => user.outgoingFriendRequests)
    incomingFriendRequests: Relation<User[]>;

    @ManyToMany(() => User, (user) => user.incomingFriendRequests, {
        cascade: true,
    })
    @JoinTable()
    outgoingFriendRequests: Relation<User[]>;

    @ManyToMany(() => User, {
        cascade: true
    })
    @JoinTable()
    friends: Relation<User[]>;

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
}
