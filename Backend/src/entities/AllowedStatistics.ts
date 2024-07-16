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

    @OneToOne(() => User, (user) => user.userPRs)
    @JoinColumn()
    user: Relation<User>;

    @Column()
    Ab_Wheel?: number;

    @Column()
    Aerobics?: number;

    @Column()
    Arnold_Press__Dumbbell?: number;

    @Column()
    Around_the_World?: number;

    @Column()
    Back_Extension?: number;

    @Column()
    Back_Extension__Machine?: number;

    @Column()
    Ball_Slams?: number;

    @Column()
    Battle_Ropes?: number;

    @Column()
    Bench_Dip?: number;

    @Column()
    Bench_Press__Barbell?: number;

    @Column()
    Bench_Press__Cable?: number;

    @Column()
    Bench_Press__Dumbbell?: number;

    @Column()
    Bench_Press__Smith_Machine?: number;

    @Column()
    Bench_Press_Close_Grip__Barbell?: number;

    @Column()
    Bench_Press_Wide_Grip__Barbell?: number;

    @Column()
    Bent_Over_One_Arm_Row__Dumbbell?: number;

    @Column()
    Bent_Over_Row__Band?: number;

    @Column()
    Bent_Over_Row__Barbell?: number;

    @Column()
    Bent_Over_Row__Dumbbell?: number;

    @Column()
    Bent_Over_Row_Underhand__Barbell?: number;

    @Column()
    Bicep_Curl__Barbell?: number;

    @Column()
    Bicep_Curl__Cable?: number;

    @Column()
    Bicep_Curl__Dumbbell?: number;

    @Column()
    Bicep_Curl__Machine?: number;

    @Column()
    Bicycle_Crunch?: number;

    @Column()
    Box_Jump?: number;

    @Column()
    Box_Squat?: number;

    @Column()
    Bulgarian_Split_Squat?: number;

    @Column()
    Burpee?: number;

    @Column()
    Cable_Crossover?: number;

    @Column()
    Cable_Crunch?: number;

    @Column()
    Cable_Kickback?: number;

    @Column()
    Cable_Pull_Through?: number;

    @Column()
    Cable_Twist?: number;

    @Column()
    Calf_Press_on_Leg_Press?: number;

    @Column()
    Calf_Press_on_Seated_Leg_Press?: number;

    @Column()
    Chest_Dip?: number;

    @Column()
    Chest_Dip__Assisted?: number;

    @Column()
    Chest_Fly__Band?: number;

    @Column()
    Chest_Fly__Dumbbell?: number;

    @Column()
    Chest_Press__Band?: number;

    @Column()
    Chest_Press__Machine?: number;

    @Column()
    Chin_Up?: number;

    @Column()
    Chin_up__Assisted?: number;

    @Column()
    Clean__Barbell?: number;

    @Column()
    Clean_and_Jerk__Barbell?: number;

    @Column()
    Climbing?: number;

    @Column()
    Concentration_Curl__Dumbbell?: number;

    @Column()
    Cross_Body_Crunch?: number;

    @Column()
    Crunch?: number;

    @Column()
    Cycling?: number;

    @Column()
    Cycling__Indoor?: number;

    @Column()
    Deadlift__Band?: number;

    @Column()
    Deadlift__Barbell?: number;

    @Column()
    Deadlift__Dumbbell?: number;

    @Column()
    Deadlift__Smith_Machine?: number;

    @Column()
    Deadlift_High_Pull__Barbell?: number;

    @Column()
    Decline_Bench_Press__Barbell?: number;

    @Column()
    Decline_Bench_Press__Dumbbell?: number;

    @Column()
    Decline_Bench_Press__Smith_Machine?: number;

    @Column()
    Decline_Crunch?: number;

    @Column()
    Deficit_Deadlift__Barbell?: number;

    @Column()
    Elliptical_Machine?: number;

    @Column()
    Face_Pull__Cable?: number;

    @Column()
    Flat_Knee_Raise?: number;

    @Column()
    Flat_Leg_Raise?: number;

    @Column()
    Floor_Press__Barbell?: number;

    @Column()
    Front_Raise__Band?: number;

    @Column()
    Front_Raise__Cable?: number;

    @Column()
    Front_Raise__Dumbbell?: number;

    @Column()
    Front_Raise__Plate?: number;

    @Column()
    Front_Squat__Barbell?: number;

    @Column()
    Glute_Ham_Raise?: number;

    @Column()
    Glute_Kickback__Machine?: number;

    @Column()
    Goblet_Squat__Kettlebell?: number;

    @Column()
    Good_Morning__Barbell?: number;

    @Column()
    Hack_Squat?: number;

    @Column()
    Hack_Sqaut__Barbell?: number;

    @Column()
    Hammer_Curl__Band?: number;

    @Column()
    Hammer_Curl__Cable?: number;

    @Column()
    Hammer_Curl__Dumbbell?: number;

    @Column()
    Handstand_Push_Up?: number;

    @Column()
    Hand_Clean__Barbell?: number;

    @Column()
    Hang_Snatch__Barbell?: number;

    @Column()
    Hanging_Knee_Raise?: number;

    @Column()
    High_Knee_Skips?: number;

    @Column()
    Hiking?: number;

    @Column()
    Hip_Abductor__Machine?: number;

    @Column()
    Hip_Adductor__Machine?: number;

    @Column()
    Hip_Thrust__Barbell?: number;

    @Column()
    Hip_Thrust__Bodyweight?: number;

    @Column()
    Incline_Bench_Press__Barbell?: number;

    @Column()
    Incline_Bench_Press__Cable?: number;

    @Column()
    Incline_Bench_Press__Dumbbell?: number;

    @Column()
    Incline_Bench_Press__Smith_Machine?: number;

    @Column()
    Incline_Chest_Fly__Dumbbell?: number;

    @Column()
    Incline_Chest_Press__Machine?: number;

    @Column()
    Incline_Curl__Dumbbell?: number;

    @Column()
    Incline_Row__Dumbbell?: number;

    @Column()
    Inverted_Row__Bodyweight?: number;

    @Column()
    Iso_Lateral_Chest_Press__Machine?: number;

    @Column()
    Iso_Lateral_Row__Machine?: number;

    @Column()
    Jackknife_Sit_Up?: number;

    @Column()
    Jump_Rope?: number;

    @Column()
    Jump_Shrug__Barbell?: number;

    @Column()
    Jump_Squat?: number;

    @Column()
    Jumping_Jack?: number;

    @Column()
    Kettlebell_Swing?: number;

    @Column()
    Kettlebell_Turkish_Get_Up?: number;

    @Column()
    Kipping_Pull_Up?: number;

    @Column()
    Knee_Raise__Captains_Chair?: number;

    @Column()
    Kneeling_Pulldown__Band?: number;

    @Column()
    Knees_to_Elbows?: number;

    @Column()
    Lat_Pulldown__Cable?: number;

    @Column()
    Lat_Pulldown__Machine?: number;

    @Column()
    Lat_Pulldown__Single_Arm?: number;

    @Column()
    Lat_Pulldown_Underhand__Band?: number;

    @Column()
    Lat_Pulldown_Underhand__Cable?: number;

    @Column()
    Lat_Pulldown_Wide_Grip__Cable?: number;

    @Column()
    Lateral_Box_Jump?: number;

    @Column()
    Lateral_Raise__Band?: number;

    @Column()
    Lateral_Raise__Cable?: number;

    @Column()
    Lateral_Raise__Dumbbell?: number;

    @Column()
    Lateral_Raise__Machine?: number;

    @Column()
    Leg_Extension__Machine?: number;

    @Column()
    Leg_Press?: number;

    @Column()
    Lunge__Barbell?: number;

    @Column()
    Lunge__Bodyweight?: number;

    @Column()
    Lunge__Dumbbell?: number;

    @Column()
    Lying_Leg_Curl__Machine?: number;

    @Column()
    Mountain_CLimber?: number;

    @Column()
    Muscle_Up?: number;

    @Column()
    Oblique_Crunch?: number;

    @Column()
    Overhead_Press__Barbell?: number;

    @Column()
    Overhead_Press__Cable?: number;

    @Column()
    Overhead_Press__Dumbbell?: number;

    @Column()
    Overhead_Press__Smith_Machine?: number;

    @Column()
    Overhead_Squat__Barbell?: number;

    @Column()
    Pec_Deck__Machine?: number;

    @Column()
    Pendlay_Row__Barbell?: number;

    @Column()
    Pistol_Squat?: number;

    @Column()
    Plank?: number;

    @Column()
    Power_Clean?: number;

    @Column()
    Power_Snatch__Barbell?: number;

    @Column()
    Preacher_Curl__Barbell?: number;

    @Column()
    Preacher_Curl__Dumbbell?: number;

    @Column()
    Preacher_Curl__Machine?: number;

    @Column()
    Press_Under__Barbell?: number;

    @Column()
    Pull_Up?: number;

    @Column()
    Pull_Up__Assisted?: number;

    @Column()
    Pull_up__Band?: number;

    @Column()
    Pullover__Dumbbell?: number;

    @Column()
    Pullover__Machine?: number;

    @Column()
    Push_Press?: number;

    @Column()
    Push_Up?: number;

    @Column()
    Push_Up__Band?: number;

    @Column()
    Push_Up__Knees?: number;

    @Column()
    Rack_Pull__Barbell?: number;

    @Column()
    Reverse_Crunch?: number;

    @Column()
    Reverse_Curl__Band?: number;

    @Column()
    Reverse_Curl__Barbell?: number;

    @Column()
    Reverse_Curl__Dumbbell?: number;

    @Column()
    Reverse_Fly__Cable?: number;

    @Column()
    Reverse_Fly__Dumbbell?: number;

    @Column()
    Reverse_Fly__Machine?: number;

    @Column()
    Reverse_Grip_Concentration_Curl__Dumbbell?: number;

    @Column()
    Reverse_Plank?: number;

    @Column()
    Romanian_Deadlift__Barbell?: number;

    @Column()
    Romanian_Deadlift__Dumbbell?: number;

    @Column()
    Rowing__Machine?: number;

    @Column()
    Running?: number;

    @Column()
    Running__Treadmill?: number;

    @Column()
    Russian_Twist?: number;

    @Column()
    Seated_Calf_Raise__Machine?: number;

    @Column()
    Seated_Calf_Raise__Plate_Loaded?: number;

    @Column()
    Seated_Leg_Curl__Machine?: number;

    @Column()
    Seated_Leg_Press__Machine?: number;

    @Column()
    Seated_Oerhead_Press__Barbell?: number;

    @Column()
    Seated_Overhead_Press__Dumbbell?: number;

    @Column()
    Seated_Palms_Up_Wrist_Curl__Dumbbell?: number;

    @Column()
    Seated_Row__Cable?: number;

    @Column()
    Seated_Row__Machine?: number;

    @Column()
    Shoulder_Press__Plate_Loaded?: number;

    @Column()
    Shrug__Barbell?: number;

    @Column()
    Shrug__Dumbbell?: number;

    @Column()
    Shrug__Machine?: number;

    @Column()
    Shrug__Smith_Machine?: number;

    @Column()
    Side_Bend__Band?: number;

    @Column()
    Side_Bend__Cable?: number;

    @Column()
    Side_Plank?: number;

    @Column()
    Single_Leg_Bridge?: number;

    @Column()
    Sit_Up?: number;

    @Column()
    Skating?: number;

    @Column()
    Skiing?: number;

    @Column()
    Skullcrusher__Dumbbell?: number;

    @Column()
    Skullcrusher__Barbell?: number;

    @Column()
    Snatch__Barbell?: number;

    @Column()
    Snatch_Pull__Barbell?: number;

    @Column()
    Snowboarding?: number;

    @Column()
    Split_Jerk__Barbell?: number;

    @Column()
    Squat__Band?: number;

    @Column()
    Squat__Barbell?: number;

    @Column()
    Squat__Bodyweight?: number;

    @Column()
    Squat__Dumbbell?: number;

    @Column()
    Squat__Machine?: number;

    @Column()
    Squat__Smith_Machine?: number;

    @Column()
    Squat_Row__Band?: number;

    @Column()
    Standing_Calf_Raise__Barbell?: number;

    @Column()
    Standing_Calf_Raise__Bodyweight?: number;

    @Column()
    Standing_Calf_Raise__Dumbbell?: number;

    @Column()
    Standing_Calf_Raise__Machine?: number;

    @Column()
    Standing_Calf_Raise__Smith_Machine?: number;

    @Column()
    Step_up?: number;

    @Column()
    Stiff_Leg_Deadlift__Barbell?: number;

    @Column()
    Stiff_Leg_Deadlift__Dumbbell?: number;

    @Column()
    Straight_Leg_Deadlift__Band?: number;

    @Column()
    Stretching?: number;

    @Column()
    Strict_Military_Press__Barbell?: number;

    @Column()
    Sumo_Deadlift__Barbell?: number;

    @Column()
    Sumo_Deadlift_High_Pull__Barbell?: number;

    @Column()
    Superman?: number;

    @Column()
    Swimming?: number;

    @Column()
    T_Bar_Row?: number;

    @Column()
    Thurster__Barbell?: number;

    @Column()
    Thruster__Kettlebell?: number;

    @Column()
    Toes_To_Bar?: number;

    @Column()
    Torso_Rotation__Machine?: number;

    @Column()
    Trap_Bar_Deadlift?: number;

    @Column()
    Triceps_Dip?: number;

    @Column()
    Triceps_Dip__Assisted?: number;

    @Column()
    Triceps_Extension?: number;

    @Column()
    Triceps_Extension__Barbell?: number;

    @Column()
    Triceps_Extension__Cable?: number;

    @Column()
    Triceps_Extension__Dumbbell?: number;

    @Column()
    Triceps_Extension__Machine?: number;

    @Column()
    Triceps_Pushdown__Cable__Straight_Bar?: number;

    @Column()
    Upright_Row__Barbell?: number;

    @Column()
    Uprgith_Row__Cable?: number;

    @Column()
    Upright_Row__Dumbbell?: number;

    @Column()
    V_up?: number;

    @Column()
    Walking?: number;

    @Column()
    Wide_Pull_Up?: number;

    @Column()
    Wrist_Roller?: number;

    @Column()
    Yoga?: number;

    @Column()
    Zercher_Squat__Barbell?: number;

}
