import { Column, Entity, JoinTable, ManyToMany, ManyToOne, PrimaryGeneratedColumn, Relation } from "typeorm";
import { User } from "./User";

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

    @ManyToMany(() => User, {
        cascade: true,
    })
    @JoinTable()
    users?: Relation<User[]>;
}
