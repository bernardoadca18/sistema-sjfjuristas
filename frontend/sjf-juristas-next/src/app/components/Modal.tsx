import { X } from 'lucide-react';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
}

const Modal = ({ isOpen, onClose, title, children }: ModalProps) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 flex justify-center items-center z-50">
      <div className="bg-card rounded-lg shadow-lg p-6 w-11/12 md:w-1/2 lg:w-1/3 animate-fade-down">
        <div className="flex justify-between items-center border-b border-border pb-3 mb-4">
          <h4 className="text-xl font-bold text-primary">{title}</h4>
          <button onClick={onClose} className="text-textSecondary hover:text-text">
            <X size={24} />
          </button>
        </div>
        <div className="text-textSecondary">
          {children}
        </div>
        <div className="text-right mt-6">
          <button
            onClick={onClose}
            className="bg-primary text-textOnPrimary px-4 py-2 rounded-custom hover:bg-primaryDark transition-colors"
          >
            Fechar
          </button>
        </div>
      </div>
    </div>
  );
};

export default Modal;