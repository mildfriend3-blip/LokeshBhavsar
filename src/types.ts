export interface GrievanceTicket {
  id: string;
  category: string;
  categoryHi: string;
  desc: string;
  location: string;
  coords: string;
  status: 'SEALED' | 'QUEUED' | 'SYNCING' | 'SYNCED';
  timestamp: string;
  hash: string;
  clarity: string;
  sizeKb: number;
}

export interface CivicCategory {
  id: string;
  en: string;
  hi: string;
  presetDesc: string;
}
