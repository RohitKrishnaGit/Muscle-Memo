import {
    Column,
    Entity,
    JoinColumn,
    OneToOne,
    PrimaryGeneratedColumn,
    Relation,
} from "typeorm";
import { User } from "./User";

export enum Role {
    ADMIN,
    USER,
}

@Entity()
export class AllowedStatistics {
    @PrimaryGeneratedColumn()
    id: number;

    @OneToOne(() => User, (user) => user.allowedStatistics)
    @JoinColumn()
    user: Relation<User>;

    @Column({ default: false }) 
    Ab_Wheel: boolean;

    @Column({ default: false })
    Aerobics: boolean;

    @Column({ default: false })
    Arnold_Press__Dumbbell: boolean;

    @Column({ default: false })
    Around_the_World: boolean;

    @Column({ default: false })
    Back_Extension: boolean;

    @Column({ default: false })
    Back_Extension__Machine: boolean;

    @Column({ default: false })
    Ball_Slams: boolean;

    @Column({ default: false })
    Battle_Ropes: boolean;

    @Column({ default: false })
    Bench_Dip: boolean;

    @Column({ default: false })
    Bench_Press__Barbell: boolean;

    @Column({ default: false })
    Bench_Press__Cable: boolean;

    @Column({ default: false })
    Bench_Press__Dumbbell: boolean;

    @Column({ default: false })
    Bench_Press__Smith_Machine: boolean;

    @Column({ default: false })
    Bench_Press_Close_Grip__Barbell: boolean;

    @Column({ default: false })
    Bench_Press_Wide_Grip__Barbell: boolean;

    @Column({ default: false })
    Bent_Over_One_Arm_Row__Dumbbell: boolean;

    @Column({ default: false })
    Bent_Over_Row__Band: boolean;

    @Column({ default: false })
    Bent_Over_Row__Barbell: boolean;

    @Column({ default: false })
    Bent_Over_Row__Dumbbell: boolean;

    @Column({ default: false })
    Bent_Over_Row_Underhand__Barbell: boolean;

    @Column({ default: false })
    Bicep_Curl__Barbell: boolean;

    @Column({ default: false })
    Bicep_Curl__Cable: boolean;

    @Column({ default: false })
    Bicep_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Bicep_Curl__Machine: boolean;

    @Column({ default: false })
    Bicycle_Crunch: boolean;

    @Column({ default: false })
    Box_Jump: boolean;

    @Column({ default: false })
    Box_Squat: boolean;

    @Column({ default: false })
    Bulgarian_Split_Squat: boolean;

    @Column({ default: false })
    Burpee: boolean;

    @Column({ default: false })
    Cable_Crossover: boolean;

    @Column({ default: false })
    Cable_Crunch: boolean;

    @Column({ default: false })
    Cable_Kickback: boolean;

    @Column({ default: false })
    Cable_Pull_Through: boolean;

    @Column({ default: false })
    Cable_Twist: boolean;

    @Column({ default: false })
    Calf_Press_on_Leg_Press: boolean;

    @Column({ default: false })
    Calf_Press_on_Seated_Leg_Press: boolean;

    @Column({ default: false })
    Chest_Dip: boolean;

    @Column({ default: false })
    Chest_Dip__Assisted: boolean;

    @Column({ default: false })
    Chest_Fly__Band: boolean;

    @Column({ default: false })
    Chest_Fly__Dumbbell: boolean;

    @Column({ default: false })
    Chest_Press__Band: boolean;

    @Column({ default: false })
    Chest_Press__Machine: boolean;

    @Column({ default: false })
    Chin_Up: boolean;

    @Column({ default: false })
    Chin_up__Assisted: boolean;

    @Column({ default: false })
    Clean__Barbell: boolean;

    @Column({ default: false })
    Clean_and_Jerk__Barbell: boolean;

    @Column({ default: false })
    Climbing: boolean;

    @Column({ default: false })
    Concentration_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Cross_Body_Crunch: boolean;

    @Column({ default: false })
    Crunch: boolean;

    @Column({ default: false })
    Cycling: boolean;

    @Column({ default: false })
    Cycling__Indoor: boolean;

    @Column({ default: false })
    Deadlift__Band: boolean;

    @Column({ default: false })
    Deadlift__Barbell: boolean;

    @Column({ default: false })
    Deadlift__Dumbbell: boolean;

    @Column({ default: false })
    Deadlift__Smith_Machine: boolean;

    @Column({ default: false })
    Deadlift_High_Pull__Barbell: boolean;

    @Column({ default: false })
    Decline_Bench_Press__Barbell: boolean;

    @Column({ default: false })
    Decline_Bench_Press__Dumbbell: boolean;

    @Column({ default: false })
    Decline_Bench_Press__Smith_Machine: boolean;

    @Column({ default: false })
    Decline_Crunch: boolean;

    @Column({ default: false })
    Deficit_Deadlift__Barbell: boolean;

    @Column({ default: false })
    Elliptical_Machine: boolean;

    @Column({ default: false })
    Face_Pull__Cable: boolean;

    @Column({ default: false })
    Flat_Knee_Raise: boolean;

    @Column({ default: false })
    Flat_Leg_Raise: boolean;

    @Column({ default: false })
    Floor_Press__Barbell: boolean;

    @Column({ default: false })
    Front_Raise__Band: boolean;

    @Column({ default: false })
    Front_Raise__Cable: boolean;

    @Column({ default: false })
    Front_Raise__Dumbbell: boolean;

    @Column({ default: false })
    Front_Raise__Plate: boolean;

    @Column({ default: false })
    Front_Squat__Barbell: boolean;

    @Column({ default: false })
    Glute_Ham_Raise: boolean;

    @Column({ default: false })
    Glute_Kickback__Machine: boolean;

    @Column({ default: false })
    Goblet_Squat__Kettlebell: boolean;

    @Column({ default: false })
    Good_Morning__Barbell: boolean;

    @Column({ default: false })
    Hack_Squat: boolean;

    @Column({ default: false })
    Hack_Sqaut__Barbell: boolean;

    @Column({ default: false })
    Hammer_Curl__Band: boolean;

    @Column({ default: false })
    Hammer_Curl__Cable: boolean;

    @Column({ default: false })
    Hammer_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Handstand_Push_Up: boolean;

    @Column({ default: false })
    Hand_Clean__Barbell: boolean;

    @Column({ default: false })
    Hang_Snatch__Barbell: boolean;

    @Column({ default: false })
    Hanging_Knee_Raise: boolean;

    @Column({ default: false })
    High_Knee_Skips: boolean;

    @Column({ default: false })
    Hiking: boolean;

    @Column({ default: false })
    Hip_Abductor__Machine: boolean;

    @Column({ default: false })
    Hip_Adductor__Machine: boolean;

    @Column({ default: false })
    Hip_Thrust__Barbell: boolean;

    @Column({ default: false })
    Hip_Thrust__Bodyweight: boolean;

    @Column({ default: false })
    Incline_Bench_Press__Barbell: boolean;

    @Column({ default: false })
    Incline_Bench_Press__Cable: boolean;

    @Column({ default: false })
    Incline_Bench_Press__Dumbbell: boolean;

    @Column({ default: false })
    Incline_Bench_Press__Smith_Machine: boolean;

    @Column({ default: false })
    Incline_Chest_Fly__Dumbbell: boolean;

    @Column({ default: false })
    Incline_Chest_Press__Machine: boolean;

    @Column({ default: false })
    Incline_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Incline_Row__Dumbbell: boolean;

    @Column({ default: false })
    Inverted_Row__Bodyweight: boolean;

    @Column({ default: false })
    Iso_Lateral_Chest_Press__Machine: boolean;

    @Column({ default: false })
    Iso_Lateral_Row__Machine: boolean;

    @Column({ default: false })
    Jackknife_Sit_Up: boolean;

    @Column({ default: false })
    Jump_Rope: boolean;

    @Column({ default: false })
    Jump_Shrug__Barbell: boolean;

    @Column({ default: false })
    Jump_Squat: boolean;

    @Column({ default: false })
    Jumping_Jack: boolean;

    @Column({ default: false })
    Kettlebell_Swing: boolean;

    @Column({ default: false })
    Kettlebell_Turkish_Get_Up: boolean;

    @Column({ default: false })
    Kipping_Pull_Up: boolean;

    @Column({ default: false })
    Knee_Raise__Captains_Chair: boolean;

    @Column({ default: false })
    Kneeling_Pulldown__Band: boolean;

    @Column({ default: false })
    Knees_to_Elbows: boolean;

    @Column({ default: false })
    Lat_Pulldown__Cable: boolean;

    @Column({ default: false })
    Lat_Pulldown__Machine: boolean;

    @Column({ default: false })
    Lat_Pulldown__Single_Arm: boolean;

    @Column({ default: false })
    Lat_Pulldown_Underhand__Band: boolean;

    @Column({ default: false })
    Lat_Pulldown_Underhand__Cable: boolean;

    @Column({ default: false })
    Lat_Pulldown_Wide_Grip__Cable: boolean;

    @Column({ default: false })
    Lateral_Box_Jump: boolean;

    @Column({ default: false })
    Lateral_Raise__Band: boolean;

    @Column({ default: false })
    Lateral_Raise__Cable: boolean;

    @Column({ default: false })
    Lateral_Raise__Dumbbell: boolean;

    @Column({ default: false })
    Lateral_Raise__Machine: boolean;

    @Column({ default: false })
    Leg_Extension__Machine: boolean;

    @Column({ default: false })
    Leg_Press: boolean;

    @Column({ default: false })
    Lunge__Barbell: boolean;

    @Column({ default: false })
    Lunge__Bodyweight: boolean;

    @Column({ default: false })
    Lunge__Dumbbell: boolean;

    @Column({ default: false })
    Lying_Leg_Curl__Machine: boolean;

    @Column({ default: false })
    Mountain_CLimber: boolean;

    @Column({ default: false })
    Muscle_Up: boolean;

    @Column({ default: false })
    Oblique_Crunch: boolean;

    @Column({ default: false })
    Overhead_Press__Barbell: boolean;

    @Column({ default: false })
    Overhead_Press__Cable: boolean;

    @Column({ default: false })
    Overhead_Press__Dumbbell: boolean;

    @Column({ default: false })
    Overhead_Press__Smith_Machine: boolean;

    @Column({ default: false })
    Overhead_Squat__Barbell: boolean;

    @Column({ default: false })
    Pec_Deck__Machine: boolean;

    @Column({ default: false })
    Pendlay_Row__Barbell: boolean;

    @Column({ default: false })
    Pistol_Squat: boolean;

    @Column({ default: false })
    Plank: boolean;

    @Column({ default: false })
    Power_Clean: boolean;

    @Column({ default: false })
    Power_Snatch__Barbell: boolean;

    @Column({ default: false })
    Preacher_Curl__Barbell: boolean;

    @Column({ default: false })
    Preacher_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Preacher_Curl__Machine: boolean;

    @Column({ default: false })
    Press_Under__Barbell: boolean;

    @Column({ default: false })
    Pull_Up: boolean;

    @Column({ default: false })
    Pull_Up__Assisted: boolean;

    @Column({ default: false })
    Pull_up__Band: boolean;

    @Column({ default: false })
    Pullover__Dumbbell: boolean;

    @Column({ default: false })
    Pullover__Machine: boolean;

    @Column({ default: false })
    Push_Press: boolean;

    @Column({ default: false })
    Push_Up: boolean;

    @Column({ default: false })
    Push_Up__Band: boolean;

    @Column({ default: false })
    Push_Up__Knees: boolean;

    @Column({ default: false })
    Rack_Pull__Barbell: boolean;

    @Column({ default: false })
    Reverse_Crunch: boolean;

    @Column({ default: false })
    Reverse_Curl__Band: boolean;

    @Column({ default: false })
    Reverse_Curl__Barbell: boolean;

    @Column({ default: false })
    Reverse_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Reverse_Fly__Cable: boolean;

    @Column({ default: false })
    Reverse_Fly__Dumbbell: boolean;

    @Column({ default: false })
    Reverse_Fly__Machine: boolean;

    @Column({ default: false })
    Reverse_Grip_Concentration_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Reverse_Plank: boolean;

    @Column({ default: false })
    Romanian_Deadlift__Barbell: boolean;

    @Column({ default: false })
    Romanian_Deadlift__Dumbbell: boolean;

    @Column({ default: false })
    Rowing__Machine: boolean;

    @Column({ default: false })
    Running: boolean;

    @Column({ default: false })
    Running__Treadmill: boolean;

    @Column({ default: false })
    Russian_Twist: boolean;

    @Column({ default: false })
    Seated_Calf_Raise__Machine: boolean;

    @Column({ default: false })
    Seated_Calf_Raise__Plate_Loaded: boolean;

    @Column({ default: false })
    Seated_Leg_Curl__Machine: boolean;

    @Column({ default: false })
    Seated_Leg_Press__Machine: boolean;

    @Column({ default: false })
    Seated_Oerhead_Press__Barbell: boolean;

    @Column({ default: false })
    Seated_Overhead_Press__Dumbbell: boolean;

    @Column({ default: false })
    Seated_Palms_Up_Wrist_Curl__Dumbbell: boolean;

    @Column({ default: false })
    Seated_Row__Cable: boolean;

    @Column({ default: false })
    Seated_Row__Machine: boolean;

    @Column({ default: false })
    Shoulder_Press__Plate_Loaded: boolean;

    @Column({ default: false })
    Shrug__Barbell: boolean;

    @Column({ default: false })
    Shrug__Dumbbell: boolean;

    @Column({ default: false })
    Shrug__Machine: boolean;

    @Column({ default: false })
    Shrug__Smith_Machine: boolean;

    @Column({ default: false })
    Side_Bend__Band: boolean;

    @Column({ default: false })
    Side_Bend__Cable: boolean;

    @Column({ default: false })
    Side_Plank: boolean;

    @Column({ default: false })
    Single_Leg_Bridge: boolean;

    @Column({ default: false })
    Sit_Up: boolean;

    @Column({ default: false })
    Skating: boolean;

    @Column({ default: false })
    Skiing: boolean;

    @Column({ default: false })
    Skullcrusher__Dumbbell: boolean;

    @Column({ default: false })
    Skullcrusher__Barbell: boolean;

    @Column({ default: false })
    Snatch__Barbell: boolean;

    @Column({ default: false })
    Snatch_Pull__Barbell: boolean;

    @Column({ default: false })
    Snowboarding: boolean;

    @Column({ default: false })
    Split_Jerk__Barbell: boolean;

    @Column({ default: false })
    Squat__Band: boolean;

    @Column({ default: false })
    Squat__Barbell: boolean;

    @Column({ default: false })
    Squat__Bodyweight: boolean;

    @Column({ default: false })
    Squat__Dumbbell: boolean;

    @Column({ default: false })
    Squat__Machine: boolean;

    @Column({ default: false })
    Squat__Smith_Machine: boolean;

    @Column({ default: false })
    Squat_Row__Band: boolean;

    @Column({ default: false })
    Standing_Calf_Raise__Barbell: boolean;

    @Column({ default: false })
    Standing_Calf_Raise__Bodyweight: boolean;

    @Column({ default: false })
    Standing_Calf_Raise__Dumbbell: boolean;

    @Column({ default: false })
    Standing_Calf_Raise__Machine: boolean;

    @Column({ default: false })
    Standing_Calf_Raise__Smith_Machine: boolean;

    @Column({ default: false })
    Step_up: boolean;

    @Column({ default: false })
    Stiff_Leg_Deadlift__Barbell: boolean;

    @Column({ default: false })
    Stiff_Leg_Deadlift__Dumbbell: boolean;

    @Column({ default: false })
    Straight_Leg_Deadlift__Band: boolean;

    @Column({ default: false })
    Stretching: boolean;

    @Column({ default: false })
    Strict_Military_Press__Barbell: boolean;

    @Column({ default: false })
    Sumo_Deadlift__Barbell: boolean;

    @Column({ default: false })
    Sumo_Deadlift_High_Pull__Barbell: boolean;

    @Column({ default: false })
    Superman: boolean;

    @Column({ default: false })
    Swimming: boolean;

    @Column({ default: false })
    T_Bar_Row: boolean;

    @Column({ default: false })
    Thurster__Barbell: boolean;

    @Column({ default: false })
    Thruster__Kettlebell: boolean;

    @Column({ default: false })
    Toes_To_Bar: boolean;

    @Column({ default: false })
    Torso_Rotation__Machine: boolean;

    @Column({ default: false })
    Trap_Bar_Deadlift: boolean;

    @Column({ default: false })
    Triceps_Dip: boolean;

    @Column({ default: false })
    Triceps_Dip__Assisted: boolean;

    @Column({ default: false })
    Triceps_Extension: boolean;

    @Column({ default: false })
    Triceps_Extension__Barbell: boolean;

    @Column({ default: false })
    Triceps_Extension__Cable: boolean;

    @Column({ default: false })
    Triceps_Extension__Dumbbell: boolean;

    @Column({ default: false })
    Triceps_Extension__Machine: boolean;

    @Column({ default: false })
    Triceps_Pushdown__Cable__Straight_Bar: boolean;

    @Column({ default: false })
    Upright_Row__Barbell: boolean;

    @Column({ default: false })
    Uprgith_Row__Cable: boolean;

    @Column({ default: false })
    Upright_Row__Dumbbell: boolean;

    @Column({ default: false })
    V_up: boolean;

    @Column({ default: false })
    Walking: boolean;

    @Column({ default: false })
    Wide_Pull_Up: boolean;

    @Column({ default: false })
    Wrist_Roller: boolean;

    @Column({ default: false })
    Yoga: boolean;

    @Column({ default: false })
    Zercher_Squat__Barbell: boolean;

}
