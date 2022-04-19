import { buildPath } from "./generic"
import { postRequest } from "./http"

const apiHost = process.env.REACT_APP_API_HOST
const apiPrefix = process.env.REACT_APP_API_PREFIX


const buildApiUrl = (path) => {
    try {
        const url = new URL(apiHost)
        url.pathname = buildPath(apiPrefix, path)
        return url.href
    } catch {
        return path
    }
}

export const register = async (data) => {
    try {
        const response = await postRequest(buildApiUrl('/auth/register'), JSON.stringify(data))

        if (response && response.success) return [true, null]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}