import axios from "axios";

export function staticRequest(config) {
    const instance = axios.create({
        baseURL:'your_url',
        timeout:5000
    })
    return instance(config)
}
