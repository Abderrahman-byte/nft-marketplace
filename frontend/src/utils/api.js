import { buildPath } from "./generic"
import { deleteRequest, getRequest, multipartPostRequest, postRequest, putRequest } from "./http"

const apiHost = process.env.REACT_APP_API_HOST
const apiPrefix = process.env.REACT_APP_API_PREFIX


export const buildWebsocketApiUrl = (path) => {
    try {
        const uri = new URL(apiHost)
        uri.pathname = path
        uri.protocol = 'ws'
        return uri.href
    } catch {
        return path
    }
}

export const buildApiUrl = (path) => {
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
    } catch {}

    return false
}

export const getProfile = async()=>{
    try{
        const response = await getRequest(buildApiUrl('/profile'))
        
        if (response && response.profile) return response.profile
    } catch {}

    return null
}

export const updateProfile = async (data) => {
    try {
        const response = await postRequest(buildApiUrl('/profile'), JSON.stringify(data))
        if (response && response.success) return [true, response.error]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}

export const saveProfilePicture = async (file, type) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        formData.append('Type',type)

        const response = await multipartPostRequest(buildApiUrl('/profile/picture'), formData)
        
        if (response && response.success) return [true, null]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}

export const getUserData = async (id) => {
    try {
        const response = await getRequest(buildApiUrl(`/user/${id}`))

        if (response && response.success && response.data) return response.data
    } catch {}

    return null
}

export const getUserForSaleTokens = async (id, limit = 10, offset = 0) => {
    try {
        const response = await getRequest(buildApiUrl(`/user/${id}/tokens`) + `?role=SALE&limit=${limit}&offset=${offset}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const getUserCreatedTokens = async (id, limit = 10, offset = 0) => {
    try {
        const response = await getRequest(buildApiUrl(`/user/${id}/tokens`) + `?role=CREATOR&limit=${limit}&offset=${offset}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const getUserOwnedTokens = async (id, limit = 10, offset = 0) => {
    try {
        const response = await getRequest(buildApiUrl(`/user/${id}/tokens`) + `?role=OWNER&limit=${limit}&offset=${offset}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const getUserFavoriteTokens = async (id, limit = 10, offset = 0) => {
    try {
        const response = await getRequest(buildApiUrl(`/user/${id}/tokens`) + `?role=FAVORITE&limit=${limit}&offset=${offset}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const postLikeToken = async (id) => {
    try {
        const response = await postRequest(buildApiUrl('/marketplace/like'), JSON.stringify({ id }))

        if (response && response.success) return true
    } catch {}

    return false
}

export const deleteLikeToken = async (id) => {
    try {
        const response = await deleteRequest(buildApiUrl('/marketplace/like') + `?id=${id}`)

        if (response && response.success) return true
    } catch {}

    return false
}

export const createCollection = async (formData) => {
    try {
        const response = await multipartPostRequest(buildApiUrl('/marketplace/collections'), formData)

        if (response && response.success && response.data) return [response.data, null]
        if (response && response.error) return [null, response.error]
    } catch {}

    return [null, null]
}

export const getUserCollections = async () => {
    try {
        const response = await getRequest(buildApiUrl('/user/collections'))

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const createToken = async (file, metadata, multi = false) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        formData.append('metadata', JSON.stringify(metadata))
        formData.append('multi', multi)

        const response = await multipartPostRequest(buildApiUrl('/marketplace/tokens'), formData)

        if (response && response.success && response.data) return [response.data, null]
        else if (response && response.error) return [null, response.error]
    } catch {}

    return [null, null]
}

export const getTokens = async (sort = 'LIKES', maxPrice = 10000, limit = 10, offset = 0) => {
    try {
        const response = await getRequest(buildApiUrl('/marketplace/tokens') + `?sort=${sort}&range=${maxPrice}&limit=${limit}&offset=${offset}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const getCollectionsList = async (limit, withDetails = false) => {
    try {
        const response = await getRequest(buildApiUrl('/marketplace/collections') + `?limit=${limit}&details=${withDetails}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const getDetailsToken = async (id) => {
    try{
        const response = await getRequest(buildApiUrl('marketplace/tokens/'+id))
        
        if(response && response.data ) return [response.data, response.data.owner, response.data.creator]
    } catch{}

    return [false, null]
}

export const createBidStream = async (data) => {
    try {
        const response = await postRequest(buildApiUrl('/marketplace/bids'), JSON.stringify(data))

        if (response && response.success && response.ref) return [response.ref, null]
        else if (response && response.error) return [null, response.error]
    } catch {}

    return [null, null]
}
export const createTransaction = async(data) =>{

    try{
        const response = await postRequest(buildApiUrl('/marketplace/buy'), JSON.stringify(data))

        if(response && response.success && response.ref) return [response.ref, null]
        else if (response && response.error) return [null, response.error]
    } catch {}
    return [null, null]
}  

export const getBidsToken = async(id, limit=10, offset=0)=>{
    try{
        const response = await getRequest(buildApiUrl(`/marketplace/tokens/${id}/bids`)+`?limit=${limit}&offset=${offset}`)
        if (response && response.success && response.data) return response.data

    }catch{}
    return[]
}

export const getHistoryTransaction = async(id, limit=10, offset=0)=>{
    try{
        const response = await getRequest(buildApiUrl(`/marketplace/tokens/${id}/transactions`)+`?limit=${limit}&offset=${offset}`)
  
        if (response && response.success && response.data) return response.data

    }catch{}
    return[]
}

export const updateTokenSettings = async (id,data) => {
    try {
        const response = await putRequest(buildApiUrl(`/marketplace/tokens/${id}`), JSON.stringify(data))

        if (response && response.success) return [true, response.error]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}

export const getPopularSellersList = async (interval) => {
    try {
        const response = await getRequest(buildApiUrl(`/user/populare`) + `?interval=${interval}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}