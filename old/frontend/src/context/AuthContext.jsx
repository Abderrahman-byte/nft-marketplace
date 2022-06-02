import React, { createContext, useEffect, useState } from 'react'

import LoadingCard from '@Components/LoadingCard'
import { getProfile, isUserLoggedIn } from '@Utils/api'

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
    const [authenticated, setAuth] = useState(undefined)
    const [account, setAccount] = useState(undefined)
    const [onExistCallback, setOnExistCallback] = useState(null)

    const [isActive, setActive] = useState(false)
    const [modelElt, setModelElt] = useState(null)

    const openModel = (elt, onExistCallback = null) => {
        setModelElt(elt)
        setActive(true)

        if (typeof onExistCallback === 'function') setOnExistCallback(() => onExistCallback)
        else setOnExistCallback(null)
    }

    const closeModel = () => {
        setModelElt(null)
        setActive(false)
        setOnExistCallback(null)
    }

    const getAuthState = async () => {
        openModel(<LoadingCard />)

        const isLoggedIn = await isUserLoggedIn()
        setAuth(isLoggedIn)
        
        closeModel()
    }

    const getAccountData = async () => {
        const profile = await getProfile()
        setAccount(profile)
    }

    const setProfileData = (data) => {
        const prev = account || {}
        setAccount({...prev, ...data})
    }

    const onExistModel = () => {
        if (typeof onExistCallback === 'function') onExistCallback()
    }

    useEffect(() => {
        if (authenticated) {
            getAccountData()
        } else {
            setAccount(null)
        }
    }, [authenticated])
    
    useEffect(() => {
        getAuthState()
    }, [])

    return (
        <AuthContext.Provider value={{ openModel, closeModel, authenticated, setAuth, account, setProfileData }}>
            {children}
            <div onClick={onExistModel} className={`model-background ${isActive ? 'active' : ''}`}></div>
            <div className={`model-popup-box ${isActive ? 'active' : ''}`}>
                {modelElt}
            </div>
        </AuthContext.Provider>
    )
}