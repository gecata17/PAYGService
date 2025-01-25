export interface User {
    id: string;
    email: string;
    role: 'USER' | 'PUBLISHER' | 'ADMIN';
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    email: string;
    password: string;
    confirmPassword: string;
}

export interface AuthResponse {
    user: User;
    token: string;
} 