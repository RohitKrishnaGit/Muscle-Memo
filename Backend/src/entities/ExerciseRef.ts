import { Column, Entity, PrimaryGeneratedColumn } from "typeorm";

@Entity()
export class ExerciseRef {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @Column()
    durationVSReps: number;

    @Column()
    weight: number;

    @Column()
    distance: number;

    @Column({ nullable: true })
    description?: string;

    @Column({ nullable: true })
    imagePath?: string;
}
