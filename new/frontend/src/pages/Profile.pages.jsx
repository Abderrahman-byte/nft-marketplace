import React from 'react'
import { Route, Routes } from 'react-router'

import Profilepage from './Profile.page'
import EditProfilePage from './EditProfile.page'

const ProfilePages = () => {
    return (
        <Routes>
            <Route path='*' element={<Profilepage/>}/>
            <Route path='edit' element={<EditProfilePage />} />
        </Routes>
    )
}

export default ProfilePages