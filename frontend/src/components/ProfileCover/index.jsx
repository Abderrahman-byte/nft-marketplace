import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

import { AuthContext } from '@/context/AuthContext'
import { saveProfilePicture } from '@/utils/api'
import LoadingCard from '../LoadingCard'

import './styles.css'

// TODO : check if user is in his profile 

const ProfileCover = ({ updateCover = false, profile }) => {
    const [coverUrl, setCoverUrl] = useState(profile?.cover)
    const { openModel, closeModel } = useContext(AuthContext)

    useEffect(() => {
        console.log(profile)
        if (!coverUrl && profile?.cover) setCoverUrl(profile.cover)
    }, [profile])

    const coverImageChanged = async (e) => {
        const files = e.target.files

        if (files.length <= 0) return

        openModel(<LoadingCard />)

        const [saved] = await saveProfilePicture(files[0], 'cover')

        const fileReader = new FileReader()

        fileReader.onload = e => setCoverUrl(e.target.result)

        fileReader.readAsDataURL(files[0])

        closeModel()
    }

    return (
        <div className='ProfileCover' style={{ 'backgroundImage': `url(${coverUrl})`}} >
            <input onChange={coverImageChanged} className='hidden' id='cover-image-input' type='file' accept='image/*' /> 
           
            {updateCover ? (<div className='buttons'>
                <label htmlFor='cover-image-input' className='btn btn-white'>Edit cover photo</label>
                <Link to='#' className='btn btn-white'>Edit cover photo</Link>
            </div>) : null}
        </div>
    )
}

export default ProfileCover