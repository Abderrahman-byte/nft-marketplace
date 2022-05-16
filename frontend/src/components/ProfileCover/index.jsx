import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

import { AuthContext } from '@Context/AuthContext'
import { saveProfilePicture } from '@Utils/api'
import LoadingCard from '@Components/LoadingCard'

import './styles.css'

const ProfileCover = ({ allowUpdate = false, profile }) => {
    const [coverUrl, setCoverUrl] = useState(profile?.cover)
    const { openModel, closeModel, setProfileData } = useContext(AuthContext)

    useEffect(() => {
        if (!coverUrl && profile?.cover) setCoverUrl(profile.cover)
    }, [profile])

    const coverImageChanged = async (e) => {
        const files = e.target.files

        if (files.length <= 0 || !allowUpdate) return

        openModel(<LoadingCard />)

        const [saved] = await saveProfilePicture(files[0], 'cover')
        
        if (saved) {
            const fileUrl = URL.createObjectURL(files[0])
            
            setProfileData({ ...profile, cover: fileUrl})
            setCoverUrl(fileUrl)    
        }

        closeModel()
    }

    return (
        <div className='ProfileCover' style={{ 'backgroundImage': `url(${coverUrl})`}} >
            <input onChange={coverImageChanged} className='hidden' id='cover-image-input' type='file' accept='image/*' /> 
            {allowUpdate ? (<div className='buttons'>
                <label htmlFor='cover-image-input' className='btn btn-white'>Edit cover photo</label>
                <Link to='/profile/edit' className='btn btn-white'>Edit Profile</Link>
            </div>) : null}
        </div>
    )
}

export default ProfileCover