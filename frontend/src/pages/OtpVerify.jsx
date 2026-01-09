import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { ShieldCheck, KeyRound, ArrowRight } from 'lucide-react';
import api from '../api';

const OtpVerify = () => {
  const [otp, setOtp] = useState('');
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  useEffect(() => {
    if (!email) {
      navigate('/login');
    }
  }, [email, navigate]);

  const handleVerify = async (e) => {
    e.preventDefault();
    try {
      await api.post('/auth/verify', { email, otp });
      // Save email for profile fetching
      localStorage.setItem('userEmail', email);
      // On success, redirect to dashboard (replace history so back button goes to login)
      navigate('/dashboard', { replace: true });
    } catch (error) {
      alert('Verification failed: ' + (error.response?.data || error.message));
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen p-4">
      <motion.div 
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        className="glass-panel p-8 w-full max-w-md"
      >
        <div className="text-center mb-8">
          <ShieldCheck className="w-12 h-12 mx-auto text-primary-500 mb-4" />
          <h2 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-violet-400">Two-Factor Auth</h2>
          <p className="text-slate-400 mt-2">Enter the code sent to {email}</p>
        </div>

        <form onSubmit={handleVerify} className="space-y-6">
          <div className="relative">
            <KeyRound className="absolute left-3 top-3 text-slate-500 w-5 h-5" />
            <input 
              type="text" 
              placeholder="Enter 6-digit OTP" 
              maxLength={6}
              className="input-field pl-10 text-center tracking-widest text-2xl"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              required
            />
          </div>
          
          <button type="submit" className="btn-primary flex items-center justify-center gap-2 group">
            Verify & Access
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </button>
        </form>
      </motion.div>
    </div>
  );
};

export default OtpVerify;
