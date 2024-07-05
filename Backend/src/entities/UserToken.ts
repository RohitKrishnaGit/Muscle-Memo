import {
    Column,
    Entity,
    JoinColumn,
    OneToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
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
