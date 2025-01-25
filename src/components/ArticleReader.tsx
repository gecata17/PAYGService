import React, { useState, useEffect, useRef } from 'react';

interface ArticleReaderProps {
    articleId: string;
    content: string;
}

const ArticleReader: React.FC<ArticleReaderProps> = ({ articleId, content }) => {
    const [readingProgress, setReadingProgress] = useState(0);
    const [timeSpent, setTimeSpent] = useState(0);
    const [lastScrollPosition, setLastScrollPosition] = useState(0);
    const contentRef = useRef<HTMLDivElement>(null);
    
    useEffect(() => {
        // Start timer when component mounts
        const timer = setInterval(() => {
            setTimeSpent(prev => prev + 1);
            // Send tracking data every 30 seconds
            if (timeSpent > 0 && timeSpent % 30 === 0) {
                sendTrackingData();
            }
        }, 1000);
        
        return () => {
            clearInterval(timer);
            // Send final tracking data when component unmounts
            sendTrackingData();
        };
    }, [timeSpent]);
    
    const handleScroll = (e: React.UIEvent<HTMLDivElement>) => {
        if (!contentRef.current) return;
        
        const element = e.target as HTMLDivElement;
        const scrollTop = element.scrollTop;
        const scrollHeight = element.scrollHeight - element.clientHeight;
        const currentProgress = Math.min((scrollTop / scrollHeight) * 100, 100);
        
        setReadingProgress(currentProgress);
        setLastScrollPosition(scrollTop);
        
        // Send tracking data when progress changes significantly (>5%)
        if (Math.abs(currentProgress - readingProgress) > 5) {
            sendTrackingData();
        }
    };
    
    const sendTrackingData = async () => {
        try {
            await fetch('/api/tracking', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    articleId,
                    progressPercentage: readingProgress,
                    timeSpentSeconds: timeSpent,
                    lastReadPosition: lastScrollPosition,
                }),
            });
        } catch (error) {
            console.error('Failed to send tracking data:', error);
        }
    };
    
    return (
        <div className="article-reader">
            <div 
                ref={contentRef}
                className="article-content" 
                onScroll={handleScroll}
                style={{ maxHeight: '80vh', overflow: 'auto' }}
            >
                {content}
            </div>
            <div className="progress-bar-container">
                <div 
                    className="progress-bar" 
                    style={{
                        width: `${readingProgress}%`,
                        height: '4px',
                        backgroundColor: '#007bff',
                        transition: 'width 0.3s ease-in-out'
                    }} 
                />
            </div>
            <div className="reading-stats">
                <span>Progress: {Math.round(readingProgress)}%</span>
                <span>Time spent: {formatTime(timeSpent)}</span>
            </div>
        </div>
    );
};

const formatTime = (seconds: number): string => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
};

export default ArticleReader; 