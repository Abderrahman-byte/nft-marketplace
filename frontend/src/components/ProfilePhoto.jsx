import React, {useState} from "react";
import { Link } from "react-router-dom";
import '../styles/ProfilePhoto.css'




const ProfilePhoto = () => {
    const [image, setimage]= useState("")
   
    const handlerupload=(e)=>
    {
        const reader = new FileReader();
        reader.onload = ()=>{
            if(reader.readyState === 2){
               setimage(reader.result)
            }
        }
        reader.readAsDataURL(e.target.files[0])
    }
    return (
       
        <div className="ProfilePhoto">

            <div className="Avatar">
            
                 <img className="Pic" src= {image } />
            </div>
            {/*<img className="Avatar" src= {CreateSingleImg } >  
            
    </img>*/}
            <div className="Frame-944">
                <div className="Frame-945">
                    <span className="Profile">Photo de profile </span>
                    <span className="Recomendation"> We recommend an image
                        of at least 400x400. Gifs work too 🙌</span>

                </div>
                <button className='btn btn-white' onClick={handlerupload}> Upload</button>
                {/*<input className='btn btn-white' type="file" id="img" name="img" accept="image/*" onChange={handlerupload} />*/}
            </div>

        </div>
    )


}

export default ProfilePhoto;