import { initializeApp } from "firebase/app";
import { getDatabase, ref, child } from "firebase/database";
import { GeoFire } from "geofire";

const firebaseConfig = {databaseURL: "https://musclememo-410f9-default-rtdb.firebaseio.com/"}
const app = initializeApp(firebaseConfig);
const database = getDatabase(app);
export const geoFire = new GeoFire(child(ref(database),"geofirekeys"));