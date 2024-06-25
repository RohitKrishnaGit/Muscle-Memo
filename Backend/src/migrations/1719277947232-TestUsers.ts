import { MigrationInterface, QueryRunner } from "typeorm";

export class TestUsers1719277947232 implements MigrationInterface {
    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`INSERT INTO user (username, fullName, email, password)
            VALUES ('starfy83', 'Derek Three', 'starfy83@uwaterloo.ca', 'verySecurePassword'),
            ('echan', 'Erik Chan', 'echan@uwaterloo.ca', 'verySecurePassword');`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`DELETE FROM user;`);
    }
}
