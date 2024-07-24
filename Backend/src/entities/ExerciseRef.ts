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

    @Column({ nullable: true }) //todo: NOT NULLABLE
    description?: string;

    @Column({ nullable: true }) //todo: NOT NULLABLE
    imagePath?: string;

    @Column({ default: false })
    isCustom: boolean;
}
