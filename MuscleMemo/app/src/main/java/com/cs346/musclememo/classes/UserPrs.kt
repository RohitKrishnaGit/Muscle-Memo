package com.cs346.musclememo.classes

import androidx.compose.runtime.mutableStateListOf

data class UserPrs(
    val id : Int,
    val Ab_Wheel: Number = 0,    
    val Aerobics: Number = 0,    
    val Arnold_Press__Dumbbell: Number = 0,    
    val Around_the_World: Number = 0,    
    val Back_Extension: Number = 0,    
    val Back_Extension__Machine: Number = 0,    
    val Ball_Slams: Number = 0,    
    val Battle_Ropes: Number = 0,    
    val Bench_Dip: Number = 0,    
    val Bench_Press__Barbell: Number = 0,    
    val Bench_Press__Cable: Number = 0,    
    val Bench_Press__Dumbbell: Number = 0,    
    val Bench_Press__Smith_Machine: Number = 0,    
    val Bench_Press_Close_Grip__Barbell: Number = 0,    
    val Bench_Press_Wide_Grip__Barbell: Number = 0,    
    val Bent_Over_One_Arm_Row__Dumbbell: Number = 0,    
    val Bent_Over_Row__Band: Number = 0,    
    val Bent_Over_Row__Barbell: Number = 0,    
    val Bent_Over_Row__Dumbbell: Number = 0,    
    val Bent_Over_Row_Underhand__Barbell: Number = 0,    
    val Bicep_Curl__Barbell: Number = 0,    
    val Bicep_Curl__Cable: Number = 0,    
    val Bicep_Curl__Dumbbell: Number = 0,    
    val Bicep_Curl__Machine: Number = 0,    
    val Bicycle_Crunch: Number = 0,    
    val Box_Jump: Number = 0,    
    val Box_Squat: Number = 0,    
    val Bulgarian_Split_Squat: Number = 0,    
    val Burpee: Number = 0,    
    val Cable_Crossover: Number = 0,    
    val Cable_Crunch: Number = 0,    
    val Cable_Kickback: Number = 0,    
    val Cable_Pull_Through: Number = 0,    
    val Cable_Twist: Number = 0,    
    val Calf_Press_on_Leg_Press: Number = 0,    
    val Calf_Press_on_Seated_Leg_Press: Number = 0,    
    val Chest_Dip: Number = 0,    
    val Chest_Dip__Assisted: Number = 0,    
    val Chest_Fly__Band: Number = 0,    
    val Chest_Fly__Dumbbell: Number = 0,    
    val Chest_Press__Band: Number = 0,    
    val Chest_Press__Machine: Number = 0,    
    val Chin_Up: Number = 0,    
    val Chin_up__Assisted: Number = 0,    
    val Clean__Barbell: Number = 0,    
    val Clean_and_Jerk__Barbell: Number = 0,    
    val Climbing: Number = 0,    
    val Concentration_Curl__Dumbbell: Number = 0,    
    val Cross_Body_Crunch: Number = 0,    
    val Crunch: Number = 0,    
    val Cycling: Number = 0,    
    val Cycling__Indoor: Number = 0,    
    val Deadlift__Band: Number = 0,    
    val Deadlift__Barbell: Number = 0,    
    val Deadlift__Dumbbell: Number = 0,    
    val Deadlift__Smith_Machine: Number = 0,    
    val Deadlift_High_Pull__Barbell: Number = 0,    
    val Decline_Bench_Press__Barbell: Number = 0,    
    val Decline_Bench_Press__Dumbbell: Number = 0,    
    val Decline_Bench_Press__Smith_Machine: Number = 0,    
    val Decline_Crunch: Number = 0,    
    val Deficit_Deadlift__Barbell: Number = 0,    
    val Elliptical_Machine: Number = 0,    
    val Face_Pull__Cable: Number = 0,    
    val Flat_Knee_Raise: Number = 0,    
    val Flat_Leg_Raise: Number = 0,    
    val Floor_Press__Barbell: Number = 0,    
    val Front_Raise__Band: Number = 0,    
    val Front_Raise__Cable: Number = 0,    
    val Front_Raise__Dumbbell: Number = 0,    
    val Front_Raise__Plate: Number = 0,    
    val Front_Squat__Barbell: Number = 0,    
    val Glute_Ham_Raise: Number = 0,    
    val Glute_Kickback__Machine: Number = 0,    
    val Goblet_Squat__Kettlebell: Number = 0,    
    val Good_Morning__Barbell: Number = 0,    
    val Hack_Squat: Number = 0,    
    val Hack_Sqaut__Barbell: Number = 0,    
    val Hammer_Curl__Band: Number = 0,    
    val Hammer_Curl__Cable: Number = 0,    
    val Hammer_Curl__Dumbbell: Number = 0,    
    val Handstand_Push_Up: Number = 0,    
    val Hand_Clean__Barbell: Number = 0,    
    val Hang_Snatch__Barbell: Number = 0,    
    val Hanging_Knee_Raise: Number = 0,    
    val High_Knee_Skips: Number = 0,    
    val Hiking: Number = 0,    
    val Hip_Abductor__Machine: Number = 0,    
    val Hip_Adductor__Machine: Number = 0,    
    val Hip_Thrust__Barbell: Number = 0,    
    val Hip_Thrust__Bodyweight: Number = 0,    
    val Incline_Bench_Press__Barbell: Number = 0,    
    val Incline_Bench_Press__Cable: Number = 0,    
    val Incline_Bench_Press__Dumbbell: Number = 0,    
    val Incline_Bench_Press__Smith_Machine: Number = 0,    
    val Incline_Chest_Fly__Dumbbell: Number = 0,    
    val Incline_Chest_Press__Machine: Number = 0,    
    val Incline_Curl__Dumbbell: Number = 0,    
    val Incline_Row__Dumbbell: Number = 0,    
    val Inverted_Row__Bodyweight: Number = 0,    
    val Iso_Lateral_Chest_Press__Machine: Number = 0,    
    val Iso_Lateral_Row__Machine: Number = 0,    
    val Jackknife_Sit_Up: Number = 0,    
    val Jump_Rope: Number = 0,    
    val Jump_Shrug__Barbell: Number = 0,    
    val Jump_Squat: Number = 0,    
    val Jumping_Jack: Number = 0,    
    val Kettlebell_Swing: Number = 0,    
    val Kettlebell_Turkish_Get_Up: Number = 0,    
    val Kipping_Pull_Up: Number = 0,    
    val Knee_Raise__Captains_Chair: Number = 0,    
    val Kneeling_Pulldown__Band: Number = 0,    
    val Knees_to_Elbows: Number = 0,    
    val Lat_Pulldown__Cable: Number = 0,    
    val Lat_Pulldown__Machine: Number = 0,    
    val Lat_Pulldown__Single_Arm: Number = 0,    
    val Lat_Pulldown_Underhand__Band: Number = 0,    
    val Lat_Pulldown_Underhand__Cable: Number = 0,    
    val Lat_Pulldown_Wide_Grip__Cable: Number = 0,    
    val Lateral_Box_Jump: Number = 0,    
    val Lateral_Raise__Band: Number = 0,    
    val Lateral_Raise__Cable: Number = 0,    
    val Lateral_Raise__Dumbbell: Number = 0,    
    val Lateral_Raise__Machine: Number = 0,    
    val Leg_Extension__Machine: Number = 0,    
    val Leg_Press: Number = 0,    
    val Lunge__Barbell: Number = 0,    
    val Lunge__Bodyweight: Number = 0,    
    val Lunge__Dumbbell: Number = 0,    
    val Lying_Leg_Curl__Machine: Number = 0,    
    val Mountain_CLimber: Number = 0,    
    val Muscle_Up: Number = 0,    
    val Oblique_Crunch: Number = 0,    
    val Overhead_Press__Barbell: Number = 0,    
    val Overhead_Press__Cable: Number = 0,    
    val Overhead_Press__Dumbbell: Number = 0,    
    val Overhead_Press__Smith_Machine: Number = 0,    
    val Overhead_Squat__Barbell: Number = 0,    
    val Pec_Deck__Machine: Number = 0,    
    val Pendlay_Row__Barbell: Number = 0,    
    val Pistol_Squat: Number = 0,    
    val Plank: Number = 0,    
    val Power_Clean: Number = 0,    
    val Power_Snatch__Barbell: Number = 0,    
    val Preacher_Curl__Barbell: Number = 0,    
    val Preacher_Curl__Dumbbell: Number = 0,    
    val Preacher_Curl__Machine: Number = 0,    
    val Press_Under__Barbell: Number = 0,    
    val Pull_Up: Number = 0,    
    val Pull_Up__Assisted: Number = 0,    
    val Pull_up__Band: Number = 0,    
    val Pullover__Dumbbell: Number = 0,    
    val Pullover__Machine: Number = 0,    
    val Push_Press: Number = 0,    
    val Push_Up: Number = 0,    
    val Push_Up__Band: Number = 0,    
    val Push_Up__Knees: Number = 0,    
    val Rack_Pull__Barbell: Number = 0,    
    val Reverse_Crunch: Number = 0,    
    val Reverse_Curl__Band: Number = 0,    
    val Reverse_Curl__Barbell: Number = 0,    
    val Reverse_Curl__Dumbbell: Number = 0,    
    val Reverse_Fly__Cable: Number = 0,    
    val Reverse_Fly__Dumbbell: Number = 0,    
    val Reverse_Fly__Machine: Number = 0,    
    val Reverse_Grip_Concentration_Curl__Dumbbell: Number = 0,    
    val Reverse_Plank: Number = 0,    
    val Romanian_Deadlift__Barbell: Number = 0,    
    val Romanian_Deadlift__Dumbbell: Number = 0,    
    val Rowing__Machine: Number = 0,    
    val Running: Number = 0,    
    val Running__Treadmill: Number = 0,    
    val Russian_Twist: Number = 0,    
    val Seated_Calf_Raise__Machine: Number = 0,    
    val Seated_Calf_Raise__Plate_Loaded: Number = 0,    
    val Seated_Leg_Curl__Machine: Number = 0,    
    val Seated_Leg_Press__Machine: Number = 0,    
    val Seated_Oerhead_Press__Barbell: Number = 0,    
    val Seated_Overhead_Press__Dumbbell: Number = 0,    
    val Seated_Palms_Up_Wrist_Curl__Dumbbell: Number = 0,    
    val Seated_Row__Cable: Number = 0,    
    val Seated_Row__Machine: Number = 0,    
    val Shoulder_Press__Plate_Loaded: Number = 0,    
    val Shrug__Barbell: Number = 0,    
    val Shrug__Dumbbell: Number = 0,    
    val Shrug__Machine: Number = 0,    
    val Shrug__Smith_Machine: Number = 0,    
    val Side_Bend__Band: Number = 0,    
    val Side_Bend__Cable: Number = 0,    
    val Side_Plank: Number = 0,    
    val Single_Leg_Bridge: Number = 0,    
    val Sit_Up: Number = 0,    
    val Skating: Number = 0,    
    val Skiing: Number = 0,    
    val Skullcrusher__Dumbbell: Number = 0,    
    val Skullcrusher__Barbell: Number = 0,    
    val Snatch__Barbell: Number = 0,    
    val Snatch_Pull__Barbell: Number = 0,    
    val Snowboarding: Number = 0,    
    val Split_Jerk__Barbell: Number = 0,    
    val Squat__Band: Number = 0,    
    val Squat__Barbell: Number = 0,    
    val Squat__Bodyweight: Number = 0,    
    val Squat__Dumbbell: Number = 0,    
    val Squat__Machine: Number = 0,    
    val Squat__Smith_Machine: Number = 0,    
    val Squat_Row__Band: Number = 0,    
    val Standing_Calf_Raise__Barbell: Number = 0,    
    val Standing_Calf_Raise__Bodyweight: Number = 0,    
    val Standing_Calf_Raise__Dumbbell: Number = 0,    
    val Standing_Calf_Raise__Machine: Number = 0,    
    val Standing_Calf_Raise__Smith_Machine: Number = 0,    
    val Step_up: Number = 0,    
    val Stiff_Leg_Deadlift__Barbell: Number = 0,    
    val Stiff_Leg_Deadlift__Dumbbell: Number = 0,    
    val Straight_Leg_Deadlift__Band: Number = 0,    
    val Stretching: Number = 0,    
    val Strict_Military_Press__Barbell: Number = 0,    
    val Sumo_Deadlift__Barbell: Number = 0,    
    val Sumo_Deadlift_High_Pull__Barbell: Number = 0,    
    val Superman: Number = 0,    
    val Swimming: Number = 0,    
    val T_Bar_Row: Number = 0,    
    val Thurster__Barbell: Number = 0,    
    val Thruster__Kettlebell: Number = 0,    
    val Toes_To_Bar: Number = 0,    
    val Torso_Rotation__Machine: Number = 0,    
    val Trap_Bar_Deadlift: Number = 0,    
    val Triceps_Dip: Number = 0,    
    val Triceps_Dip__Assisted: Number = 0,    
    val Triceps_Extension: Number = 0,    
    val Triceps_Extension__Barbell: Number = 0,    
    val Triceps_Extension__Cable: Number = 0,    
    val Triceps_Extension__Dumbbell: Number = 0,    
    val Triceps_Extension__Machine: Number = 0,    
    val Triceps_Pushdown__Cable__Straight_Bar: Number = 0,    
    val Upright_Row__Barbell: Number = 0,    
    val Uprgith_Row__Cable: Number = 0,    
    val Upright_Row__Dumbbell: Number = 0,    
    val V_up: Number = 0,    
    val Walking: Number = 0,    
    val Wide_Pull_Up: Number = 0,    
    val Wrist_Roller: Number = 0,    
    val Yoga: Number = 0,    
    val Zercher_Squat__Barbell: Number = 0,
)