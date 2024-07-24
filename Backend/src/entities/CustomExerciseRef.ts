import {
    Column,
    Entity,
    ManyToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { User } from "./User";

@Entity()
export class CustomExerciseRef {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @Column()
    durationVSReps: boolean;

    @Column()
    weight: boolean;

    @Column()
    distance: boolean;

    @Column({ nullable: true })
    description?: string;

    @Column({ nullable: true })
    imagePath?: string;

    @ManyToOne(() => User, (user) => user)
    user: Relation<User>;

    @Column({ default: true })
    isCustom: boolean;
}
