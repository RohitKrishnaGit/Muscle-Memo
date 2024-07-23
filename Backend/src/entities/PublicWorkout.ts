import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
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
    location: string;

    @Column({ nullable: true })
    gender?: string;

    @Column({ nullable: true })
    experience?: string;

    @ManyToOne(() => User, (user) => user.workouts)
    creator: User;
}
