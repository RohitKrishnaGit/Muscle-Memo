import { AppDataSource } from "../src/data-source";
import { Role, User } from "../src/entities/User";
import { generatePasswordHash } from "../src/utils/password";

AppDataSource.initialize()
    .then(async () => {
        const args = process.argv.slice(2);
        if (args.length !== 3) {
            console.error(
                'expected 3 args, username and password...\nExample Usage: npm run createAdmin "username" "password" "em@il.com"'
            );
            return;
        }
        const [username, password, email] = args;
        const userRepository = AppDataSource.getRepository(User);

        const userExists = !!(await userRepository.findOneBy([
            { username },
            { email },
        ]));

        if (userExists) {
            console.error(
                "There already exists a user with the same username or email"
            );
            return;
        }

        const user = Object.assign(new User(), {
            username,
            fullName: "admin",
            email,
            password: await generatePasswordHash(password),
            role: Role.ADMIN,
        });

        await userRepository.save(user);

        console.log("Admin created");
    })
    .catch((err) => {
        console.error("Error during Data Source initialization", err);
    });
