import { buildPath } from "./generic"
import { getRequest, multipartPostRequest, postRequest } from "./http"

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

export const sendLogin = async (username, password) => {
    try {
        const response = await postRequest(buildApiUrl('/auth/login'), JSON.stringify({ username, password }))
        
        if (response && response.success) return [true, null]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}

export const isUserLoggedIn = async () => {
    try {
        const response = await getRequest(buildApiUrl('/auth/isLoggedIn'))

        if (response && response.isLoggedIn) return true
    } catch (err) {
        console.log("[ERROR] " + err)
    }

    return false
}

export const getProfile = async()=>{
    try{
        const response = await getRequest(buildApiUrl('/profile'))
        console.log(response);
        console.log(response.profile)
         return [response.success, response.profile]
    }
    catch{}
    return [false, null]
    
}
export const sendProfile = async (item) => {
    try {
        const response = await postRequest(buildApiUrl('/profile'), JSON.stringify(item))
        if (response && response.success) return [true, null]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}
export const sendProfilepicture = async (file, type) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        formData.append('Type',type)
        const response = await multipartPostRequest(buildApiUrl('marketplace/profile/picture'), formData)
        if (response && response.success) return [true, null]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}