import React from 'react'

import AvatarLink from '@Components/AvatarLink'
import './styles.css'

const DetailsInfo = ({ owner, creator }) => {
    return (
        <div className="DetailsInfo ">
            <AvatarLink title='Owner' size="medium" name={owner?.displayName || owner?.username} to={`/user/${owner?.id}`} img={owner?.avatarUrl} />
            
            <div className="horizontal-divider"></div>

            <AvatarLink title='Creator' size="medium" name={creator?.displayName || creator?.username} to={`/user/${owner?.id}`} img={creator?.avatarUrl} />
            
            <div className="horizontal-divider"></div>
        </div>
    )
}
export default DetailsInfo;