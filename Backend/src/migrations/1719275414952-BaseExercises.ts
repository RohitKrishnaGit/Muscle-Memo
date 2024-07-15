import { MigrationInterface, QueryRunner } from "typeorm";

export class BaseExercises1719275414952 implements MigrationInterface {
    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(
            `INSERT INTO exercise_ref (name, durationVSReps, weight, distance)
            VALUES
                ('Ab Wheel', 0, 0, 0),
                ('Aerobics', 1, 0, 1),
                ('Arnold Press (Dumbbell)', 0, 1, 0),
                ('Around the World', 0, 0, 0),
                ('Back Extension', 0, 0, 0),
                ('Back Extension (Machine)', 0, 1, 0),
                ('Ball Slams', 0, 1, 0),
                ('Battle Ropes', 1, 0, 0),
                ('Bench Dip', 0, 0, 0),
                ('Bench Press (Barbell)', 0, 1, 0),
                ('Bench Press (Cable)', 0, 1, 0),
                ('Bench Press (Dumbbell)', 0, 1, 0),
                ('Bench Press (Smith Machine)', 0, 1, 0),
                ('Bench Press - Close Grip (Barbell)', 0, 1, 0),
                ('Bench Press - Wide Grip (Barbell)', 0, 1, 0),
                ('Bent Over One Arm Row (Dumbbell)', 0, 1, 0),
                ('Bent Over Row (Band)', 0, 0, 0),
                ('Bent Over Row (Barbell)', 0, 1, 0),
                ('Bent Over Row (Dumbbell)', 0, 1, 0),
                ('Bent Over Row - Underhand (Barbell)', 0, 1, 0),
                ('Bicep Curl (Barbell)', 0, 1, 0),
                ('Bicep Curl (Cable)', 0, 1, 0),
                ('Bicep Curl (Dumbbell)', 0, 1, 0),
                ('Bicep Curl (Machine)', 0, 1, 0),
                ('Bicycle Crunch', 1, 0, 0),
                ('Box Jump', 0, 0, 0),
                ('Box Squat', 0, 0, 0),
                ('Bulgarian Split Squat', 0, 0, 0),
                ('Burpee', 1, 0, 0),
                ('Cable Crossover', 0, 1, 0),
                ('Cable Crunch', 0, 1, 0),
                ('Cable Kickback', 0, 1, 0),
                ('Cable Pull Through', 0, 1, 0),
                ('Cable Twist', 0, 1, 0),
                ('Calf Press on Leg Press', 0, 1, 0),
                ('Calf Press on Seated Leg Press', 0, 1, 0),
                ('Chest Dip', 0, 0, 0),
                ('Chest Dip (Assisted)', 0, 1, 0),
                ('Chest Fly (Band)', 0, 0, 0),
                ('Chest Fly (Dumbbell)', 0, 1, 0),
                ('Chest Press (Band)', 0, 0, 0),
                ('Chest Press (Machine)', 0, 1, 0),
                ('Chin Up', 0, 0, 0),
                ('Chin up (Assisted)', 0, 1, 0),
                ('Clean (Barbell)', 0, 1, 0),
                ('Clean and Jerk (Barbell)', 0, 1, 0),
                ('Climbing', 1, 0, 1),
                ('Concentration Curl (Dumbbell)', 0, 1, 0),
                ('Cross Body Crunch', 1, 0, 0),
                ('Crunch', 1, 0, 0),
                ('Cycling', 1, 0, 1),
                ('Cycling (Indoor)', 1, 0, 1),
                ('Deadlift (Band)', 0, 0, 0),
                ('Deadlift (Barbell)', 0, 1, 0),
                ('Deadlift (Dumbbell)', 0, 1, 0),
                ('Deadlift (Smith Machine)', 0, 1, 0),
                ('Deadlift High Pull (Barbell)', 0, 1, 0),
                ('Decline Bench Press (Barbell)', 0, 1, 0),
                ('Decline Bench Press (Dumbbell)', 0, 1, 0),
                ('Decline Bench Press (Smith Machine)', 0, 1, 0),
                ('Decline Crunch', 1, 0, 0),
                ('Deficit Deadlift (Barbell)', 0, 1, 0),
                ('Elliptical Machine', 1, 0, 0),
                ('Face Pull (Cable)', 0, 1, 0),
                ('Flat Knee Raise', 0, 0, 0),
                ('Flat Leg Raise', 0, 0, 0),
                ('Floor Press (Barbell)', 0, 1, 0),
                ('Front Raise (Band)', 0, 0, 0),
                ('Front Raise (Cable)', 0, 1, 0),
                ('Front Raise (Dumbbell)', 0, 1, 0),
                ('Front Raise (Plate)', 0, 1, 0),
                ('Front Squat (Barbell)', 0, 1, 0),
                ('Glute Ham Raise', 0, 0, 0),
                ('Glute Kickback (Machine)', 0, 1, 0),
                ('Goblet Squat (Kettlebell)', 0, 1, 0),
                ('Good Morning (Barbell)', 0, 1, 0),
                ('Hack Squat', 0, 1, 0),
                ('Hack Squat (Barbell)', 0, 1, 0),
                ('Hammer Curl (Band)', 0, 0, 0),
                ('Hammer Curl (Cable)', 0, 1, 0),
                ('Hammer Curl (Dumbbell)', 0, 1, 0),
                ('Handstand Push Up', 0, 0, 0),
                ('Hand Clean (Barbell)', 0, 1, 0),
                ('Hang Snatch (Barbell)', 0, 1, 0),
                ('Hanging Knee Raise', 0, 0, 0),
                ('High Knee Skips', 1, 0, 0),
                ('Hiking', 1, 0, 1),
                ('Hip Abductor (Machine)', 0, 1, 0),
                ('Hip Adductor (Machine)', 0, 1, 0),
                ('Hip Thrust (Barbell)', 0, 1, 0),
                ('Hip Thrust (Bodyweight)', 0, 0, 0),
                ('Incline Bench Press (Barbell)', 0, 1, 0),
                ('Incline Bench Press (Cable)', 0, 1, 0),
                ('Incline Bench Press (Dumbbell)', 0, 1, 0),
                ('Incline Bench Press (Smith Machine)', 0, 1, 0),
                ('Incline Chest Fly (Dumbbell)', 0, 1, 0),
                ('Incline Chest Press (Machine)', 0, 1, 0),
                ('Incline Curl (Dumbbell)', 0, 1, 0),
                ('Incline Row (Dumbbell)', 0, 1, 0),
                ('Inverted Row (Bodyweight)', 0, 0, 0),
                ('Iso-Lateral Chest Press (Machine)', 0, 1, 0),
                ('Iso-Lateral Row (Machine)', 0, 1, 0),
                ('Jackknife Sit Up', 1, 0, 0),
                ('Jump Rope', 1, 0, 0),
                ('Jump Shrug (Barbell)', 0, 1, 0),
                ('Jump Squat', 0, 0, 0),
                ('Jumping Jack', 1, 0, 0),
                ('Kettlebell Swing', 0, 1, 0),
                ('Kettlebell Turkish Get Up', 0, 1, 0),
                ('Kipping Pull Up', 0, 0, 0),
                ('Knee Raise (Captain''s Chair)', 0, 0, 0),
                ('Kneeling Pulldown (Band)', 0, 0, 0),
                ('Knees to Elbows', 1, 0, 0),
                ('Lat Pulldown (Cable)', 0, 1, 0),
                ('Lat Pulldown (Machine)', 0, 1, 0),
                ('Lat Pulldown (Single Arm)', 0, 1, 0),
                ('Lat Pulldown - Underhand (Band)', 0, 0, 0),
                ('Lat Pulldown - Underhand (Cable)', 0, 1, 0),
                ('Lat Pulldown - Wide Grip (Cable)', 0, 1, 0),
                ('Lateral Box Jump', 0, 0, 0),
                ('Lateral Raise (Band)', 0, 0, 0),
                ('Lateral Raise (Cable)', 0, 1, 0),
                ('Lateral Raise (Dumbbell)', 0, 1, 0),
                ('Lateral Raise (Machine)', 0, 1, 0),
                ('Leg Extension (Machine)', 0, 1, 0),
                ('Leg Press', 0, 1, 0),
                ('Lunge (Barbell)', 0, 1, 0),
                ('Lunge (Bodyweight)', 0, 0, 0),
                ('Lunge (Dumbbell)', 0, 1, 0),
                ('Lying Leg Curl (Machine)', 0, 1, 0),
                ('Mountain Climber', 1, 0, 0),
                ('Muscle Up', 0, 0, 0),
                ('Oblique Crunch', 1, 0, 0),
                ('Overhead Press (Barbell)', 0, 1, 0),
                ('Overhead Press (Cable)', 0, 1, 0),
                ('Overhead Press (Dumbbell)', 0, 1, 0),
                ('Overhead Press (Smith Machine)', 0, 1, 0),
                ('Overhead Squat (Barbell)', 0, 1, 0),
                ('Pec Deck (Machine)', 0, 1, 0),
                ('Pendlay Row (Barbell)', 0, 1, 0),
                ('Pistol Squat', 0, 0, 0),
                ('Plank', 1, 0, 0),
                ('Power Clean', 0, 1, 0),
                ('Power Snatch (Barbell)', 0, 1, 0),
                ('Preacher Curl (Barbell)', 0, 1, 0),
                ('Preacher Curl (Dumbbell)', 0, 1, 0),
                ('Preacher Curl (Machine)', 0, 1, 0),
                ('Press Under (Barbell)', 0, 1, 0),
                ('Pull Up', 0, 0, 0),
                ('Pull Up (Assisted)', 0, 1, 0),
                ('Pull up (Band)', 0, 0, 0),
                ('Pullover (Dumbbell)', 0, 1, 0),
                ('Pullover (Machine)', 0, 1, 0),
                ('Push Press', 0, 1, 0),
                ('Push Up', 0, 0, 0),
                ('Push Up (Band)', 0, 0, 0),
                ('Push Up (Knees)', 0, 0, 0),
                ('Rack Pull (Barbell)', 0, 1, 0),
                ('Reverse Crunch', 1, 0, 0),
                ('Reverse Curl (Band)', 0, 0, 0),
                ('Reverse Curl (Barbell)', 0, 1, 0),
                ('Reverse Curl (Dumbbell)', 0, 1, 0),
                ('Reverse Fly (Cable)', 0, 1, 0),
                ('Reverse Fly (Dumbbell)', 0, 1, 0),
                ('Reverse Fly (Machine)', 0, 1, 0),
                ('Reverse Grip Concentration Curl (Dumbbell)', 0, 1, 0),
                ('Reverse Plank', 1, 0, 0),
                ('Romanian Deadlift (Barbell)', 0, 1, 0),
                ('Romanian Deadlift (Dumbbell)', 0, 1, 0),
                ('Rowing (Machine)', 1, 0, 1),
                ('Running', 1, 0, 1),
                ('Running (Treadmill)', 1, 0, 1),
                ('Russian Twist', 1, 0, 0),
                ('Seated Calf Raise (Machine)', 0, 1, 0),
                ('Seated Calf Raise (Plate Loaded)', 0, 1, 0),
                ('Seated Leg Curl (Machine)', 0, 1, 0),
                ('Seated Leg Press (Machine)', 0, 1, 0),
                ('Seated Overhead Press (Barbell)', 0, 1, 0),
                ('Seated Overhead Press (Dumbbell)', 0, 1, 0),
                ('Seated Palms Up Wrist Curl (Dumbbell)', 0, 1, 0),
                ('Seated Row (Cable)', 0, 1, 0),
                ('Seated Row (Machine)', 0, 1, 0),
                ('Shoulder Press (Plate Loaded)', 0, 1, 0),
                ('Shrug (Barbell)', 0, 1, 0),
                ('Shrug (Dumbbell)', 0, 1, 0),
                ('Shrug (Machine)', 0, 1, 0),
                ('Shrug (Smith Machine)', 0, 1, 0),
                ('Side Bend (Band)', 0, 0, 0),
                ('Side Bend (Cable)', 0, 1, 0),
                ('Side Plank', 1, 0, 0),
                ('Single Leg Bridge', 0, 0, 0),
                ('Sit Up', 1, 0, 0),
                ('Skating', 1, 0, 1),
                ('Skiing', 1, 0, 1),
                ('Skullcrusher (Dumbbell)', 0, 1, 0),
                ('Skullcrusher (Barbell)', 0, 1, 0),
                ('Snatch (Barbell)', 0, 1, 0),
                ('Snatch Pull (Barbell)', 0, 1, 0),
                ('Snowboarding', 1, 0, 1),
                ('Split Jerk (Barbell)', 0, 1, 0),
                ('Squat (Band)', 0, 0, 0),
                ('Squat (Barbell)', 0, 1, 0),
                ('Squat (Bodyweight)', 0, 0, 0),
                ('Squat (Dumbbell)', 0, 1, 0),
                ('Squat (Machine)', 0, 1, 0),
                ('Squat (Smith Machine)', 0, 1, 0),
                ('Squat Row (Band)', 0, 0, 0),
                ('Standing Calf Raise (Barbell)', 0, 1, 0),
                ('Standing Calf Raise (Bodyweight)', 0, 0, 0),
                ('Standing Calf Raise (Dumbbell)', 0, 1, 0),
                ('Standing Calf Raise (Machine)', 0, 1, 0),
                ('Standing Calf Raise (Smith Machine)', 0, 1, 0),
                ('Step-up', 0, 0, 0),
                ('Stiff Leg Deadlift (Barbell)', 0, 1, 0),
                ('Stiff Leg Deadlift (Dumbbell)', 0, 1, 0),
                ('Straight Leg Deadlift (Band)', 0, 0, 0),
                ('Stretching', 1, 0, 0),
                ('Strict Military Press (Barbell)', 0, 1, 0),
                ('Sumo Deadlift (Barbell)', 0, 1, 0),
                ('Sumo Deadlift High Pull (Barbell)', 0, 1, 0),
                ('Superman', 1, 0, 0),
                ('Swimming', 1, 0, 1),
                ('T Bar Row', 0, 1, 0),
                ('Thruster (Barbell)', 0, 1, 0),
                ('Thruster (Kettlebell)', 0, 1, 0),
                ('Toes To Bar', 0, 0, 0),
                ('Torso Rotation (Machine)', 0, 1, 0),
                ('Trap Bar Deadlift', 0, 1, 0),
                ('Triceps Dip', 0, 0, 0),
                ('Triceps Dip (Assisted)', 0, 1, 0),
                ('Triceps Extension', 0, 1, 0),
                ('Triceps Extension (Barbell)', 0, 1, 0),
                ('Triceps Extension (Cable)', 0, 1, 0),
                ('Triceps Extension (Dumbbell)', 0, 1, 0),
                ('Triceps Extension (Machine)', 0, 1, 0),
                ('Triceps Pushdown (Cable- Straight Bar)', 0, 1, 0),
                ('Upright Row (Barbell)', 0, 1, 0),
                ('Upright Row (Cable)', 0, 1, 0),
                ('Upright Row (Dumbbell)', 0, 1, 0),
                ('V up', 1, 0, 0),
                ('Walking', 1, 0, 1),
                ('Wide Pull Up', 0, 0, 0),
                ('Wrist Roller', 0, 0, 0),
                ('Yoga', 1, 0, 0),
                ('Zercher Squat (Barbell)', 0, 1, 0);
        `);
        
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`DELETE FROM exercise_ref;`);
    }
}
