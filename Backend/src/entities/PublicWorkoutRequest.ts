import {
    Column,
    Entity,
    ManyToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { PublicWorkout } from "./PublicWorkout";
import { User } from "./User";

@Entity()
export class PublicWorkoutRequest {
    @PrimaryGeneratedColumn()
    id: number;

    @ManyToOne(() => User)
    sender: Relation<User>;

    @ManyToOne(() => PublicWorkout)
    publicWorkout: Relation<PublicWorkout>;
}
