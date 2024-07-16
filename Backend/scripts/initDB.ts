import { AppDataSource } from "../src/data-source";

const clearDatabase = async () => {
    const entities = AppDataSource.entityMetadatas;

    for await (const entity of entities) {
        const repository = AppDataSource.getRepository(entity.name);
        await repository.query(`DELETE FROM ${entity.tableName};`);
    }
    const migrationTableExists = (
        await AppDataSource.query(
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='migrations';"
        )
    )[0]["count(*)"];
    if (migrationTableExists) {
        await AppDataSource.query(`DELETE FROM migrations;`);
    }
    await AppDataSource.query(`DELETE FROM sqlite_sequence;`);
};

AppDataSource.initialize()
    .then(() => {
        clearDatabase();
    })
    .catch((err) => {
        console.error("Error during Data Source initialization", err);
    });
