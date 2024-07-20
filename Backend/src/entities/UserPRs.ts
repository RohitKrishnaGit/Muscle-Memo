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
export class UserPRs {
    @PrimaryGeneratedColumn()
    id: number;

    @OneToOne(() => User, (user) => user.userPRs)
    @JoinColumn()
    user: Relation<User>;

    @Column({ default: 0 })
    Ab_Wheel: number;

    @Column({ default: 0 })
    Aerobics: number;

    @Column({ default: 0 })
    Arnold_Press__Dumbbell: number;

    @Column({ default: 0 })
    Around_the_World: number;

    @Column({ default: 0 })
    Back_Extension: number;

    @Column({ default: 0 })
    Back_Extension__Machine: number;

    @Column({ default: 0 })
    Ball_Slams: number;

    @Column({ default: 0 })
    Battle_Ropes: number;

    @Column({ default: 0 })
    Bench_Dip: number;

    @Column({ default: 0 })
    Bench_Press__Barbell: number;

    @Column({ default: 0 })
    Bench_Press__Cable: number;

    @Column({ default: 0 })
    Bench_Press__Dumbbell: number;

    @Column({ default: 0 })
    Bench_Press__Smith_Machine: number;

    @Column({ default: 0 })
    Bench_Press_Close_Grip__Barbell: number;

    @Column({ default: 0 })
    Bench_Press_Wide_Grip__Barbell: number;

    @Column({ default: 0 })
    Bent_Over_One_Arm_Row__Dumbbell: number;

    @Column({ default: 0 })
    Bent_Over_Row__Band: number;

    @Column({ default: 0 })
    Bent_Over_Row__Barbell: number;

    @Column({ default: 0 })
    Bent_Over_Row__Dumbbell: number;

    @Column({ default: 0 })
    Bent_Over_Row_Underhand__Barbell: number;

    @Column({ default: 0 })
    Bicep_Curl__Barbell: number;

    @Column({ default: 0 })
    Bicep_Curl__Cable: number;

    @Column({ default: 0 })
    Bicep_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Bicep_Curl__Machine: number;

    @Column({ default: 0 })
    Bicycle_Crunch: number;

    @Column({ default: 0 })
    Box_Jump: number;

    @Column({ default: 0 })
    Box_Squat: number;

    @Column({ default: 0 })
    Bulgarian_Split_Squat: number;

    @Column({ default: 0 })
    Burpee: number;

    @Column({ default: 0 })
    Cable_Crossover: number;

    @Column({ default: 0 })
    Cable_Crunch: number;

    @Column({ default: 0 })
    Cable_Kickback: number;

    @Column({ default: 0 })
    Cable_Pull_Through: number;

    @Column({ default: 0 })
    Cable_Twist: number;

    @Column({ default: 0 })
    Calf_Press_on_Leg_Press: number;

    @Column({ default: 0 })
    Calf_Press_on_Seated_Leg_Press: number;

    @Column({ default: 0 })
    Chest_Dip: number;

    @Column({ default: 0 })
    Chest_Dip__Assisted: number;

    @Column({ default: 0 })
    Chest_Fly__Band: number;

    @Column({ default: 0 })
    Chest_Fly__Dumbbell: number;

    @Column({ default: 0 })
    Chest_Press__Band: number;

    @Column({ default: 0 })
    Chest_Press__Machine: number;

    @Column({ default: 0 })
    Chin_Up: number;

    @Column({ default: 0 })
    Chin_up__Assisted: number;

    @Column({ default: 0 })
    Clean__Barbell: number;

    @Column({ default: 0 })
    Clean_and_Jerk__Barbell: number;

    @Column({ default: 0 })
    Climbing: number;

    @Column({ default: 0 })
    Concentration_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Cross_Body_Crunch: number;

    @Column({ default: 0 })
    Crunch: number;

    @Column({ default: 0 })
    Cycling: number;

    @Column({ default: 0 })
    Cycling__Indoor: number;

    @Column({ default: 0 })
    Deadlift__Band: number;

    @Column({ default: 0 })
    Deadlift__Barbell: number;

    @Column({ default: 0 })
    Deadlift__Dumbbell: number;

    @Column({ default: 0 })
    Deadlift__Smith_Machine: number;

    @Column({ default: 0 })
    Deadlift_High_Pull__Barbell: number;

    @Column({ default: 0 })
    Decline_Bench_Press__Barbell: number;

    @Column({ default: 0 })
    Decline_Bench_Press__Dumbbell: number;

    @Column({ default: 0 })
    Decline_Bench_Press__Smith_Machine: number;

    @Column({ default: 0 })
    Decline_Crunch: number;

    @Column({ default: 0 })
    Deficit_Deadlift__Barbell: number;

    @Column({ default: 0 })
    Elliptical_Machine: number;

    @Column({ default: 0 })
    Face_Pull__Cable: number;

    @Column({ default: 0 })
    Flat_Knee_Raise: number;

    @Column({ default: 0 })
    Flat_Leg_Raise: number;

    @Column({ default: 0 })
    Floor_Press__Barbell: number;

    @Column({ default: 0 })
    Front_Raise__Band: number;

    @Column({ default: 0 })
    Front_Raise__Cable: number;

    @Column({ default: 0 })
    Front_Raise__Dumbbell: number;

    @Column({ default: 0 })
    Front_Raise__Plate: number;

    @Column({ default: 0 })
    Front_Squat__Barbell: number;

    @Column({ default: 0 })
    Glute_Ham_Raise: number;

    @Column({ default: 0 })
    Glute_Kickback__Machine: number;

    @Column({ default: 0 })
    Goblet_Squat__Kettlebell: number;

    @Column({ default: 0 })
    Good_Morning__Barbell: number;

    @Column({ default: 0 })
    Hack_Squat: number;

    @Column({ default: 0 })
    Hack_Sqaut__Barbell: number;

    @Column({ default: 0 })
    Hammer_Curl__Band: number;

    @Column({ default: 0 })
    Hammer_Curl__Cable: number;

    @Column({ default: 0 })
    Hammer_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Handstand_Push_Up: number;

    @Column({ default: 0 })
    Hand_Clean__Barbell: number;

    @Column({ default: 0 })
    Hang_Snatch__Barbell: number;

    @Column({ default: 0 })
    Hanging_Knee_Raise: number;

    @Column({ default: 0 })
    High_Knee_Skips: number;

    @Column({ default: 0 })
    Hiking: number;

    @Column({ default: 0 })
    Hip_Abductor__Machine: number;

    @Column({ default: 0 })
    Hip_Adductor__Machine: number;

    @Column({ default: 0 })
    Hip_Thrust__Barbell: number;

    @Column({ default: 0 })
    Hip_Thrust__Bodyweight: number;

    @Column({ default: 0 })
    Incline_Bench_Press__Barbell: number;

    @Column({ default: 0 })
    Incline_Bench_Press__Cable: number;

    @Column({ default: 0 })
    Incline_Bench_Press__Dumbbell: number;

    @Column({ default: 0 })
    Incline_Bench_Press__Smith_Machine: number;

    @Column({ default: 0 })
    Incline_Chest_Fly__Dumbbell: number;

    @Column({ default: 0 })
    Incline_Chest_Press__Machine: number;

    @Column({ default: 0 })
    Incline_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Incline_Row__Dumbbell: number;

    @Column({ default: 0 })
    Inverted_Row__Bodyweight: number;

    @Column({ default: 0 })
    Iso_Lateral_Chest_Press__Machine: number;

    @Column({ default: 0 })
    Iso_Lateral_Row__Machine: number;

    @Column({ default: 0 })
    Jackknife_Sit_Up: number;

    @Column({ default: 0 })
    Jump_Rope: number;

    @Column({ default: 0 })
    Jump_Shrug__Barbell: number;

    @Column({ default: 0 })
    Jump_Squat: number;

    @Column({ default: 0 })
    Jumping_Jack: number;

    @Column({ default: 0 })
    Kettlebell_Swing: number;

    @Column({ default: 0 })
    Kettlebell_Turkish_Get_Up: number;

    @Column({ default: 0 })
    Kipping_Pull_Up: number;

    @Column({ default: 0 })
    Knee_Raise__Captains_Chair: number;

    @Column({ default: 0 })
    Kneeling_Pulldown__Band: number;

    @Column({ default: 0 })
    Knees_to_Elbows: number;

    @Column({ default: 0 })
    Lat_Pulldown__Cable: number;

    @Column({ default: 0 })
    Lat_Pulldown__Machine: number;

    @Column({ default: 0 })
    Lat_Pulldown__Single_Arm: number;

    @Column({ default: 0 })
    Lat_Pulldown_Underhand__Band: number;

    @Column({ default: 0 })
    Lat_Pulldown_Underhand__Cable: number;

    @Column({ default: 0 })
    Lat_Pulldown_Wide_Grip__Cable: number;

    @Column({ default: 0 })
    Lateral_Box_Jump: number;

    @Column({ default: 0 })
    Lateral_Raise__Band: number;

    @Column({ default: 0 })
    Lateral_Raise__Cable: number;

    @Column({ default: 0 })
    Lateral_Raise__Dumbbell: number;

    @Column({ default: 0 })
    Lateral_Raise__Machine: number;

    @Column({ default: 0 })
    Leg_Extension__Machine: number;

    @Column({ default: 0 })
    Leg_Press: number;

    @Column({ default: 0 })
    Lunge__Barbell: number;

    @Column({ default: 0 })
    Lunge__Bodyweight: number;

    @Column({ default: 0 })
    Lunge__Dumbbell: number;

    @Column({ default: 0 })
    Lying_Leg_Curl__Machine: number;

    @Column({ default: 0 })
    Mountain_CLimber: number;

    @Column({ default: 0 })
    Muscle_Up: number;

    @Column({ default: 0 })
    Oblique_Crunch: number;

    @Column({ default: 0 })
    Overhead_Press__Barbell: number;

    @Column({ default: 0 })
    Overhead_Press__Cable: number;

    @Column({ default: 0 })
    Overhead_Press__Dumbbell: number;

    @Column({ default: 0 })
    Overhead_Press__Smith_Machine: number;

    @Column({ default: 0 })
    Overhead_Squat__Barbell: number;

    @Column({ default: 0 })
    Pec_Deck__Machine: number;

    @Column({ default: 0 })
    Pendlay_Row__Barbell: number;

    @Column({ default: 0 })
    Pistol_Squat: number;

    @Column({ default: 0 })
    Plank: number;

    @Column({ default: 0 })
    Power_Clean: number;

    @Column({ default: 0 })
    Power_Snatch__Barbell: number;

    @Column({ default: 0 })
    Preacher_Curl__Barbell: number;

    @Column({ default: 0 })
    Preacher_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Preacher_Curl__Machine: number;

    @Column({ default: 0 })
    Press_Under__Barbell: number;

    @Column({ default: 0 })
    Pull_Up: number;

    @Column({ default: 0 })
    Pull_Up__Assisted: number;

    @Column({ default: 0 })
    Pull_up__Band: number;

    @Column({ default: 0 })
    Pullover__Dumbbell: number;

    @Column({ default: 0 })
    Pullover__Machine: number;

    @Column({ default: 0 })
    Push_Press: number;

    @Column({ default: 0 })
    Push_Up: number;

    @Column({ default: 0 })
    Push_Up__Band: number;

    @Column({ default: 0 })
    Push_Up__Knees: number;

    @Column({ default: 0 })
    Rack_Pull__Barbell: number;

    @Column({ default: 0 })
    Reverse_Crunch: number;

    @Column({ default: 0 })
    Reverse_Curl__Band: number;

    @Column({ default: 0 })
    Reverse_Curl__Barbell: number;

    @Column({ default: 0 })
    Reverse_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Reverse_Fly__Cable: number;

    @Column({ default: 0 })
    Reverse_Fly__Dumbbell: number;

    @Column({ default: 0 })
    Reverse_Fly__Machine: number;

    @Column({ default: 0 })
    Reverse_Grip_Concentration_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Reverse_Plank: number;

    @Column({ default: 0 })
    Romanian_Deadlift__Barbell: number;

    @Column({ default: 0 })
    Romanian_Deadlift__Dumbbell: number;

    @Column({ default: 0 })
    Rowing__Machine: number;

    @Column({ default: 0 })
    Running: number;

    @Column({ default: 0 })
    Running__Treadmill: number;

    @Column({ default: 0 })
    Russian_Twist: number;

    @Column({ default: 0 })
    Seated_Calf_Raise__Machine: number;

    @Column({ default: 0 })
    Seated_Calf_Raise__Plate_Loaded: number;

    @Column({ default: 0 })
    Seated_Leg_Curl__Machine: number;

    @Column({ default: 0 })
    Seated_Leg_Press__Machine: number;

    @Column({ default: 0 })
    Seated_Oerhead_Press__Barbell: number;

    @Column({ default: 0 })
    Seated_Overhead_Press__Dumbbell: number;

    @Column({ default: 0 })
    Seated_Palms_Up_Wrist_Curl__Dumbbell: number;

    @Column({ default: 0 })
    Seated_Row__Cable: number;

    @Column({ default: 0 })
    Seated_Row__Machine: number;

    @Column({ default: 0 })
    Shoulder_Press__Plate_Loaded: number;

    @Column({ default: 0 })
    Shrug__Barbell: number;

    @Column({ default: 0 })
    Shrug__Dumbbell: number;

    @Column({ default: 0 })
    Shrug__Machine: number;

    @Column({ default: 0 })
    Shrug__Smith_Machine: number;

    @Column({ default: 0 })
    Side_Bend__Band: number;

    @Column({ default: 0 })
    Side_Bend__Cable: number;

    @Column({ default: 0 })
    Side_Plank: number;

    @Column({ default: 0 })
    Single_Leg_Bridge: number;

    @Column({ default: 0 })
    Sit_Up: number;

    @Column({ default: 0 })
    Skating: number;

    @Column({ default: 0 })
    Skiing: number;

    @Column({ default: 0 })
    Skullcrusher__Dumbbell: number;

    @Column({ default: 0 })
    Skullcrusher__Barbell: number;

    @Column({ default: 0 })
    Snatch__Barbell: number;

    @Column({ default: 0 })
    Snatch_Pull__Barbell: number;

    @Column({ default: 0 })
    Snowboarding: number;

    @Column({ default: 0 })
    Split_Jerk__Barbell: number;

    @Column({ default: 0 })
    Squat__Band: number;

    @Column({ default: 0 })
    Squat__Barbell: number;

    @Column({ default: 0 })
    Squat__Bodyweight: number;

    @Column({ default: 0 })
    Squat__Dumbbell: number;

    @Column({ default: 0 })
    Squat__Machine: number;

    @Column({ default: 0 })
    Squat__Smith_Machine: number;

    @Column({ default: 0 })
    Squat_Row__Band: number;

    @Column({ default: 0 })
    Standing_Calf_Raise__Barbell: number;

    @Column({ default: 0 })
    Standing_Calf_Raise__Bodyweight: number;

    @Column({ default: 0 })
    Standing_Calf_Raise__Dumbbell: number;

    @Column({ default: 0 })
    Standing_Calf_Raise__Machine: number;

    @Column({ default: 0 })
    Standing_Calf_Raise__Smith_Machine: number;

    @Column({ default: 0 })
    Step_up: number;

    @Column({ default: 0 })
    Stiff_Leg_Deadlift__Barbell: number;

    @Column({ default: 0 })
    Stiff_Leg_Deadlift__Dumbbell: number;

    @Column({ default: 0 })
    Straight_Leg_Deadlift__Band: number;

    @Column({ default: 0 })
    Stretching: number;

    @Column({ default: 0 })
    Strict_Military_Press__Barbell: number;

    @Column({ default: 0 })
    Sumo_Deadlift__Barbell: number;

    @Column({ default: 0 })
    Sumo_Deadlift_High_Pull__Barbell: number;

    @Column({ default: 0 })
    Superman: number;

    @Column({ default: 0 })
    Swimming: number;

    @Column({ default: 0 })
    T_Bar_Row: number;

    @Column({ default: 0 })
    Thurster__Barbell: number;

    @Column({ default: 0 })
    Thruster__Kettlebell: number;

    @Column({ default: 0 })
    Toes_To_Bar: number;

    @Column({ default: 0 })
    Torso_Rotation__Machine: number;

    @Column({ default: 0 })
    Trap_Bar_Deadlift: number;

    @Column({ default: 0 })
    Triceps_Dip: number;

    @Column({ default: 0 })
    Triceps_Dip__Assisted: number;

    @Column({ default: 0 })
    Triceps_Extension: number;

    @Column({ default: 0 })
    Triceps_Extension__Barbell: number;

    @Column({ default: 0 })
    Triceps_Extension__Cable: number;

    @Column({ default: 0 })
    Triceps_Extension__Dumbbell: number;

    @Column({ default: 0 })
    Triceps_Extension__Machine: number;

    @Column({ default: 0 })
    Triceps_Pushdown__Cable__Straight_Bar: number;

    @Column({ default: 0 })
    Upright_Row__Barbell: number;

    @Column({ default: 0 })
    Uprgith_Row__Cable: number;

    @Column({ default: 0 })
    Upright_Row__Dumbbell: number;

    @Column({ default: 0 })
    V_up: number;

    @Column({ default: 0 })
    Walking: number;

    @Column({ default: 0 })
    Wide_Pull_Up: number;

    @Column({ default: 0 })
    Wrist_Roller: number;

    @Column({ default: 0 })
    Yoga: number;

    @Column({ default: 0 })
    Zercher_Squat__Barbell: number;

}
