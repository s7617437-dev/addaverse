import 'package:flutter/material.dart';

void main() {
  runApp(const AddaVerseApp());
}

class AddaVerseApp extends StatelessWidget {
  const AddaVerseApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'AddaVerse',
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: Colors.deepPurple,
      ),
      home: const HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('AddaVerse'),
      ),
      body: const Center(
        child: Text(
          'Welcome to AddaVerse',
          style: TextStyle(fontSize: 24),
        ),
      ),
    );
  }
}
