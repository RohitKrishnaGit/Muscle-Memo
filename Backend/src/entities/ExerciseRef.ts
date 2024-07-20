import { Column, Entity, PrimaryGeneratedColumn } from "typeorm";

@Entity()
export class ExerciseRef {
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
}
