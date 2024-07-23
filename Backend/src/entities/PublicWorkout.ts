import { Column, Entity, JoinTable, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn, Relation } from "typeorm";
import { User } from "./User";
import { PublicWorkoutRequest } from "./PublicWorkoutRequest";

@Entity()
export class PublicWorkout {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @Column({ default: Date.now })
    date: number;

    @Column()
    description: string;

    @Column()
    latitude: string;

    @Column()
    longitude: string;

    @Column({ nullable: true })
    gender?: string;

    @Column({ nullable: true })
    experience?: string;

    @ManyToOne(() => User, (user) => user.publicWorkouts)
    creator: User;

    @OneToMany(() => PublicWorkoutRequest, (publicWorkoutRequest) => publicWorkoutRequest.publicWorkout, {
        cascade: true
    })
    publicWorkoutRequests: Relation<PublicWorkoutRequest[]>;

    @ManyToMany(() => User, {
        cascade: true,
    })
    @JoinTable()
    users?: Relation<User[]>;
}
