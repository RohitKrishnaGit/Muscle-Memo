import "reflect-metadata";
import { DataSource } from "typeorm";

export const AppDataSource = new DataSource({
    type: "sqlite",
    database: "database.sqlite",
    synchronize: true,
    logging: false,
    entities: ["src/entities/*{.ts,.js}"],
    migrations: ["src/migrations/*{.ts,.js}"],
    subscribers: [],
});
