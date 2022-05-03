import React from 'react'

const HeartIconSVG = ({ className = '' }) => {
    return (
        <svg className={className} width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path fillRule="evenodd" clipRule="evenodd" d="M10.577 5.764a.833.833 0 0 1-1.154 0l-.577-.554A3.75 3.75 0 0 0 2.5 7.916c0 1.986 1.075 3.626 2.626 4.973 1.553 1.348 3.41 2.242 4.52 2.699a.914.914 0 0 0 .708 0c1.11-.457 2.966-1.351 4.52-2.7 1.55-1.346 2.626-2.986 2.626-4.971a3.75 3.75 0 0 0-6.346-2.707l-.577.554zM10 4.008A5.417 5.417 0 0 0 .833 7.917c0 5.306 5.809 8.237 8.178 9.212a2.58 2.58 0 0 0 1.977 0c2.37-.975 8.178-3.906 8.178-9.212A5.417 5.417 0 0 0 10 4.008z" fill="#777E91"/>
        </svg>
    )
}

export default HeartIconSVG