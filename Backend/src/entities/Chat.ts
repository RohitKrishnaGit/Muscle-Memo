import {
    Column,
    Entity,
    ManyToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { User } from "./User";

@Entity()
export class Chat {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    roomId: string;

    @Column()
    message: string;

    @ManyToOne(() => User, (user) => user)
    sender: Relation<User>;
}