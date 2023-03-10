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
        
        if (response && response.success && response.data) return [response.data, null]
        else if (response && response.error) return [null, response.error]
    } catch {}

    return [null, null]
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
        const response = await getRequest(buildApiUrl('/auth/profile'))
        
        if (response && response.data) return response.data
    } catch {}

    return null
}

export const updateProfile = async (data) => {
    try {
        const response = await putRequest(buildApiUrl('/auth/profile'), JSON.stringify(data))
        if (response && response.success) return [true, response.error]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}

export const saveProfilePicture = async (file, type) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        formData.append('type',type)

        const response = await multipartPostRequest(buildApiUrl('/auth/profile/picture'), formData)
        
        if (response && response.success) return [true, null]
        else if (response && response.error) return [false, response.error]
    } catch {}

    return [false, null]
}

export const getUserData = async (id) => {
    try {
        const response = await getRequest(buildApiUrl(`/users/${id}`))

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
        const response = await postRequest(buildApiUrl(`/marketplace/tokens/${id}/like`), JSON.stringify({}))

        if (response && response.success) return true
    } catch {}

    return false
}

export const deleteLikeToken = async (id) => {
    try {
        const response = await deleteRequest(buildApiUrl(`/marketplace/tokens/${id}/like`), JSON.stringify({}))

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
        const response = await getRequest(buildApiUrl('/marketplace/user/collections'))

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const createToken = async (file, metadata) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        formData.append('metadata', JSON.stringify(metadata))

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

export const getCollectionsList = async (limit = 10) => {
    try {
        const response = await getRequest(buildApiUrl('/marketplace/collections') + `?limit=${limit}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const getDetailsToken = async (id) => {
    try{
        const response = await getRequest(buildApiUrl('marketplace/tokens/'+id))
        
        if(response && response.data ) return response.data
    } catch{}

    return null
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

        if(response && response.success && response.data?.ref) return [response.data.ref, null]
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

export const respondOffer = async (id, data)=>{
    try{
        const response = await postRequest(buildApiUrl(`/marketplace/bids/${id}`), JSON.stringify(data))
        if(response && response.success) return [true, response.error]
        else if (response && response.error) return [false, response.error]
    }catch{}
}

export const getPopularSellersList = async (interval) => {
    try {
        const response = await getRequest(buildApiUrl(`/user/populare`) + `?interval=${interval}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const getCollectionDetails = async (id) => {
    try {
        const response = await getRequest(buildApiUrl(`/marketplace/collections/${id}`))

        if (response && response.success && response.data) return response.data
    } catch {}

    return null
}

export const getCollectionTokens = async (id, limit, offset) => {
    try {
        const response = await getRequest(buildApiUrl(`/marketplace/collections/${id}/tokens`) + `?limit=${limit}&offset=${offset}`)

        if (response && response.success && response.data) return response.data
    } catch {}

    return []
}

export const makeSearch = (sub) => {
    return async (query, page) => {
        try {
            const response = await getRequest(buildApiUrl(`/search/${sub}`) + `?query=${query}&page=${page}`)
    
            if (response.success && response.data) return response.data
        } catch {}
    
        return []
    }
}

export const searchItems = makeSearch('items')

export const searchCollections = makeSearch('collections')

export const searchUsers = makeSearch('users')