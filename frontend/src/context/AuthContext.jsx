import React, { createContext, useEffect, useState } from 'react'

import LoadingCard from '@Components/LoadingCard'
import { getProfile, isUserLoggedIn } from '@Utils/api'

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
    const [authenticated, setAuth] = useState(undefined)
    const [account, setAccount] = useState(undefined)

    const [isActive, setActive] = useState(false)
    const [modelElt, setModelElt] = useState(null)

    const openModel = (elt) => {
        setModelElt(elt)
        setActive(true)
    }

    const closeModel = () => {
        setModelElt(null)
        setActive(false)
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
            <div className={`model-background ${isActive ? 'active' : ''}`}></div>
            <div className={`model-popup-box ${isActive ? 'active' : ''}`}>
                {modelElt}
            </div>
        </AuthContext.Provider>
    )
}