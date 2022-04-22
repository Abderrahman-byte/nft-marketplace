import React from "react";
import { Link } from "react-router-dom";

import '../styles/ProfilePhoto.css'




const ProfilePhoto = () => {


    return (
        <div className="ProfilePhoto">

            <div className="Avatar">
            <div className="Pic">

             </div>

            </div>
            <div className="Frame-944">
                <div className="Frame-945">
                    <span className="Profile">Photo de profile </span>
                    <span className="Recomendation"> We recommend an image
                        of at least 400x400. Gifs work too 🙌</span>

                </div>
                <Link className='btn btn-white' to='#'> Upload</Link>
            </div>

        </div>
    )


}

export default ProfilePhoto;